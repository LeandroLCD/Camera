package com.leandrolcd.data.repositories

import android.util.Log
import com.leandrolcd.data.core.local.CameraDao
import com.leandrolcd.data.core.local.Entities.DeviceEntity
import com.leandrolcd.data.mapping.toModel
import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.useCase.IDeviceRepository
import com.leandrolcd.onvifcamera.OnvifDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val cameraDao: CameraDao
) : IDeviceRepository {

    override suspend fun getDeviceByChannel(id: Int): Device? {
        val device = cameraDao.getDeviceByChannel(id)
        val isOnline = device?.let {
            OnvifDevice.isReachableEndpoint(it.url)
        } ?: false
        return cameraDao.getDeviceByChannel(id)?.toModel(isOnline)
    }

    override fun getDeviceById(id: Int): Flow<Device> {
        return cameraDao.getDeviceFlow(id).map {
            it.toModel(OnvifDevice.isReachableEndpoint(it.url))
        }
    }

    override fun getDeviceList(): Flow<List<Device>> =
        cameraDao.getDevices().transform { list ->
            list.forEach {
                Log.d("DeviceRepository", "getDeviceList: $it")
            }
            emit(list.map {
                it.toModel(OnvifDevice.isReachableEndpoint(it.url))
            })
        }

    override fun deleteDevice(device: Device) {
        cameraDao.delete(device.id)
    }

    override fun updateDevice(device: Device) {
        val entity = cameraDao.getDevice(device.id)
        if (entity != null) {
            val new = DeviceEntity(
                id = entity.id,
                serial = device.serial,
                alias = device.alias,
                canal = device.canal,
                user = entity.user,
                password = entity.password,
                url = entity.url,
                snapshot = device.snapshot
            )
            cameraDao.updateDevice(new)

        }

    }

}