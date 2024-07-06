package com.leandrolcd.data.repositories

import android.util.Log
import com.leandrolcd.core.local.Entities.DeviceEntity
import com.leandrolcd.data.BaseRepository
import com.leandrolcd.data.dto.DeviceDto
import com.leandrolcd.domain.DeviceNotFoundException
import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.useCase.IOnvifDeviceRepository
import com.leandrolcd.onvifcamera.OnvifDevice
import kotlinx.coroutines.CoroutineDispatcher
import java.net.URI
import javax.inject.Inject

class OnvifDeviceRepository @Inject constructor(
    @com.leandrolcd.core.di.DispatchersIO dispatchers: CoroutineDispatcher,
    private val cameraDao: com.leandrolcd.core.local.CameraDao
) : BaseRepository(dispatchers), IOnvifDeviceRepository {

    private val deviceMap = mutableMapOf<String, DeviceDto>()

    override suspend fun connectionDevice(
        url: String,
        user: String,
        password: String
    ): Result<String> {
        return makeNetworkCall {
            val dto = OnvifDevice.requestDevice(url, user, password, true)
            val deviceInformation = dto.getDeviceInformation()
            deviceMap[deviceInformation.serialNumber] = DeviceDto(
                user = user,
                password = password,
                url = url,
                onvif = dto
            )
            deviceInformation.serialNumber
        }
    }

    override suspend fun connectionDevice(device: Device): Result<String> {
        return makeNetworkCall {
            val entity = cameraDao.getDevice(device.id)
            val dto = OnvifDevice.requestDevice(entity!!.url, entity.user, entity.password, true)
            val deviceInformation = dto.getDeviceInformation()
            deviceMap[deviceInformation.serialNumber] = DeviceDto(
                user = entity.user,
                password = entity.password,
                url = entity.url,
                onvif = dto
            )
            deviceInformation.serialNumber
        }
    }

    override suspend fun getStreamUrl(serial: String): Result<URI> {
        return makeNetworkCall {
            if (!deviceMap.contains(serial)) {
                throw DeviceNotFoundException()
            }
            val deviceOnvif = deviceMap[serial]!!.onvif
            val profiles = deviceOnvif.getProfiles()
            Log.d("CameraViewModel", "profiles: $profiles")
            profiles.first { it.canStream() }.run {
                val uri = deviceOnvif.getStreamURI(this)
                Log.d("CameraViewModel", "uri: $uri")
                URI(uri)
            }
        }
    }

    override suspend fun getSnapshotUrl(serial: String): Result<URI> {
        return makeNetworkCall {
            if (!deviceMap.contains(serial)) {
                throw DeviceNotFoundException()
            }
            val deviceOnvif = deviceMap[serial]!!.onvif
            val profiles = deviceOnvif.getProfiles()
            profiles.first { it.canSnapshot() }.run {
               val uri = deviceOnvif.getSnapshotURI(this)
                URI(uri)
            }
        }
    }

    override suspend fun saveDevice(serial: String, canal: Int):Result<Unit> {
       return makeNetworkCall {
            val deviceOnvif = deviceMap[serial]
            if (deviceOnvif != null) {
                val entity = DeviceEntity(
                    id = 0,
                    canal = canal,
                    serial = serial,
                    alias = "Cam ${serial.take(6)}",
                    user = deviceOnvif.user,
                    password = deviceOnvif.password,
                    url = deviceOnvif.url
                )
                cameraDao.save(entity)
            }
        }

    }

}