package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import kotlinx.coroutines.flow.Flow

interface IDeviceRepository {
    suspend fun getDeviceList(): Flow<Device>
    fun deleteDevice(device: Device)
    fun updateDevice(device: Device): Boolean
}