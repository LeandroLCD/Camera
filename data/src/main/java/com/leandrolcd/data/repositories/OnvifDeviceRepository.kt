package com.leandrolcd.data.repositories

import com.leandrolcd.data.BaseRepository
import com.leandrolcd.data.dto.DeviceDto
import com.leandrolcd.data.models.ResultType
import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.models.ResultTypeUiState
import com.leandrolcd.domain.useCase.IOnvifDeviceRepository
import com.leandrolcd.onvifcamera.OnvifDevice
import kotlinx.coroutines.CoroutineDispatcher
import java.net.URI
import javax.inject.Inject

class OnvifDeviceRepository @Inject constructor(
    @com.leandrolcd.core.di.DispatchersIO dispatchers: CoroutineDispatcher,
    private val cameraDao: com.leandrolcd.core.local.CameraDao
): BaseRepository(dispatchers), IOnvifDeviceRepository {

    private val deviceMap = mutableMapOf<String, DeviceDto>()

    override suspend fun connectionDevice(url:String, user:String, password:String): ResultTypeUiState<String> {
        val result = makeNetworkCall {
            OnvifDevice.requestDevice(url, user, password, true)
        }
        return when(result){
            is ResultType.Error -> {
                ResultTypeUiState.Error(result.message)
            }
            is ResultType.Success -> {
                val deviceInformation = result.data.getDeviceInformation()
                deviceMap[deviceInformation.serialNumber] = DeviceDto(
                    user = user,
                    password=password,
                    url=url,
                    onvif = result.data
                )
                ResultTypeUiState.Success( deviceInformation.serialNumber)
            }
        }
    }

    override suspend fun connectionDevice(device: Device): ResultTypeUiState<String> {
        val entity = cameraDao.getDevice(device.id)
        val result = makeNetworkCall {
            OnvifDevice.requestDevice(entity!!.url, entity.user, entity.password, true)
        }
        return when(result){
            is ResultType.Error -> {
                ResultTypeUiState.Error(result.message)
            }
            is ResultType.Success -> {
                val deviceInformation = result.data.getDeviceInformation()
                deviceMap[deviceInformation.serialNumber] = DeviceDto(
                    user = entity!!.user,
                    password= entity.password,
                    url= entity.url,
                    onvif = result.data
                )
                ResultTypeUiState.Success( deviceInformation.serialNumber)
            }
        }
    }

    override suspend fun getStreamUrl(serial: String): ResultTypeUiState<URI> {
        if(!deviceMap.contains(serial)){
            return ResultTypeUiState.Error("Serial invalid")
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
                ResultTypeUiState.Error(result.message)
            }
            is ResultType.Success -> {
                ResultTypeUiState.Success(URI(result.data))
            }
        }
    }

    override suspend fun getSnapshotUrl(serial: String): ResultTypeUiState<URI> {
        if(!deviceMap.contains(serial)){
            return ResultTypeUiState.Error("Serial invalid")
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
                ResultTypeUiState.Error(result.message)
            }
            is ResultType.Success -> {
                ResultTypeUiState.Success(URI(result.data))
            }
        }
    }

    override fun saveDevice(serial: String, canal:Int){
        val deviceOnvif = deviceMap[serial]
       if(deviceOnvif != null){
            val entity = com.leandrolcd.core.local.Entities.DeviceEntity(
                id = 0,
                canal = canal,
                user = deviceOnvif.user,
                password = deviceOnvif.password,
                url = deviceOnvif.url
            )
            cameraDao.save(entity)
        }
    }

}