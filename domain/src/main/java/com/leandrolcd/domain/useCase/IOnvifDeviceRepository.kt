package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.models.ResultTypeUiState
import java.net.URI

interface IOnvifDeviceRepository {
    suspend fun connectionDevice(url: String, user: String, password: String): ResultTypeUiState<String>
    suspend fun connectionDevice(device: Device): ResultTypeUiState<String>
    suspend fun getStreamUrl(serial: String): ResultTypeUiState<URI>
    suspend fun getSnapshotUrl(serial: String): ResultTypeUiState<URI>
    fun saveDevice(serial: String, canal:Int)
}