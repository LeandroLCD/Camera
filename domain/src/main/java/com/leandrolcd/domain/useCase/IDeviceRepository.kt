package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import kotlinx.coroutines.flow.Flow


interface IDeviceRepository {
    suspend fun getDeviceByChannel(id: Int):Device?

    fun getDeviceById(id:Int):Flow<Device>

    fun getDeviceList(): Flow<List<Device>>

    fun deleteDevice(device: Device)

    fun updateDevice(device: Device)

}