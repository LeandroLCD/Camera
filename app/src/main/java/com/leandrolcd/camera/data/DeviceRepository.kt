package com.leandrolcd.camera.data

import com.leandrolcd.camera.core.local.CameraDao
import com.leandrolcd.camera.core.local.Entities.DeviceEntity
import com.leandrolcd.camera.data.mapping.toModel
import com.leandrolcd.camera.domain.models.Device
import com.leandrolcd.camera.domain.useCase.IDeviceRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val cameraDao: CameraDao
) : IDeviceRepository {
    override suspend fun getDeviceList() = cameraDao.getDevices().map {
        it.toModel()
    }

    override fun deleteDevice(device: Device){
       cameraDao.delete(device.id)
    }

    override fun updateDevice(device: Device):Boolean{
        val entity = cameraDao.getDevice(device.id)
       return if(entity != null){
            val new = DeviceEntity(
                id= entity.id,
                canal = device.canal,
                user = entity.user,
                password = entity.password,
                url = entity.url,
                snapshot = device.snapshot
            )
            cameraDao.updateDevice(new)
            true
        }else false

    }

}