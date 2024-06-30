package com.leandrolcd.camera.domain.useCase

import com.leandrolcd.camera.domain.models.Device
import kotlinx.coroutines.flow.Flow

interface IDeviceRepository {
    suspend fun getDeviceList(): Flow<Device>
    fun deleteDevice(device: Device)
    fun updateDevice(device: Device): Boolean
}