package com.leandrolcd.camera.data

import com.leandrolcd.camera.core.di.DispatchersIO
import com.leandrolcd.camera.core.local.CameraDao
import com.leandrolcd.camera.core.local.Entities.DeviceEntity
import com.leandrolcd.camera.data.dto.DeviceDto
import com.leandrolcd.camera.data.models.ResultType
import com.leandrolcd.camera.domain.models.Device
import com.leandrolcd.camera.domain.useCase.IOnvifDeviceRepository
import com.leandrolcd.onvifcamera.OnvifDevice
import kotlinx.coroutines.CoroutineDispatcher
import java.net.URI
import javax.inject.Inject

class OnvifDeviceRepository @Inject constructor(
    @DispatchersIO dispatchers: CoroutineDispatcher,
    private val cameraDao: CameraDao):BaseRepository(dispatchers), IOnvifDeviceRepository {

    private val deviceMap = mutableMapOf<String, DeviceDto>()

    override suspend fun connectionDevice(url:String, user:String, password:String):ResultType<String>{
        val result = makeNetworkCall {
            OnvifDevice.requestDevice(url, user, password, true)
        }
        return when(result){
            is ResultType.Error -> {
                ResultType.Error(result.message)
            }
            is ResultType.Success -> {
                val deviceInformation = result.data.getDeviceInformation()
                deviceMap[deviceInformation.serialNumber] = DeviceDto(
                    user = user,
                    password=password,
                    url=url,
                    onvif = result.data
                )
                ResultType.Success( deviceInformation.serialNumber)
            }
        }
    }

    override suspend fun connectionDevice(device: Device):ResultType<String>{
        val entity = cameraDao.getDevice(device.id)
        val result = makeNetworkCall {
            OnvifDevice.requestDevice(entity!!.url, entity.user, entity.password, true)
        }
        return when(result){
            is ResultType.Error -> {
                ResultType.Error(result.message)
            }
            is ResultType.Success -> {
                val deviceInformation = result.data.getDeviceInformation()
                deviceMap[deviceInformation.serialNumber] = DeviceDto(
                    user = entity!!.user,
                    password= entity.password,
                    url= entity.url,
                    onvif = result.data
                )
                ResultType.Success( deviceInformation.serialNumber)
            }
        }
    }

    override suspend fun getStreamUrl(serial: String):ResultType<URI>{
        if(!deviceMap.contains(serial)){
            return ResultType.Error("Serial invalid")
        }
        val result = makeNetworkCall {
            val deviceOnvif =  deviceMap[serial]!!.onvif
           val profiles = deviceOnvif.getProfiles()
            profiles.firstOrNull { it.canStream() }.let {profile ->
                if(profile == null){
                    throw Exception("The device does not have profiles to transmit video")
                }else{
                    deviceOnvif.getStreamURI(profile)
                }
            }
        }
        return when(result){
            is ResultType.Error -> {
                ResultType.Error(result.message)
            }
            is ResultType.Success -> {
                ResultType.Success(URI(result.data))
            }
        }
    }

    override suspend fun getSnapshotUrl(serial: String):ResultType<URI>{
        if(!deviceMap.contains(serial)){
            return ResultType.Error("Serial invalid")
        }
        val result = makeNetworkCall {
            val deviceOnvif =  deviceMap[serial]!!.onvif
            val profiles = deviceOnvif.getProfiles()
            profiles.firstOrNull { it.canSnapshot() }.let {profile ->
                if(profile == null){
                    throw Exception("The device does not have profiles to transmit Snapshot")
                }else{
                    deviceOnvif.getSnapshotURI(profile)
                }
            }
        }
        return when(result){
            is ResultType.Error -> {
                ResultType.Error(result.message)
            }
            is ResultType.Success -> {
                ResultType.Success(URI(result.data))
            }
        }
    }

    override fun saveDevice(serial: String, canal:Int):Int{
        val deviceOnvif = deviceMap[serial]
       return if(deviceOnvif != null){
            val entity = DeviceEntity(
                id= 0,
                canal = canal,
                user = deviceOnvif.user,
                password = deviceOnvif.password,
                url = deviceOnvif.url
            )
            cameraDao.save(entity)
        } else 0
    }

}