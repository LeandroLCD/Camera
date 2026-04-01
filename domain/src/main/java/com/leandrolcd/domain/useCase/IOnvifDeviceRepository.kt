package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.models.ResultTypeUiState
import java.net.URI


interface IOnvifDeviceRepository {
    suspend fun connectionDevice(url: String, user: String, password: String): Result<String>
    suspend fun connectionDevice(device: Device): Result<String>
    suspend fun getStreamUrl(serial: String): Result<URI>
    suspend fun getSnapshotUrl(serial: String): Result<URI>
    suspend fun saveDevice(serial: String, canal: Int):Result<Unit>
}