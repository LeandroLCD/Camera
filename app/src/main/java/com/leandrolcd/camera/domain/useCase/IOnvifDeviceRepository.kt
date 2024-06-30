package com.leandrolcd.camera.domain.useCase

import com.leandrolcd.camera.data.models.ResultType
import com.leandrolcd.camera.domain.models.Device
import java.net.URI

interface IOnvifDeviceRepository {
    suspend fun connectionDevice(url: String, user: String, password: String): ResultType<String>
    suspend fun connectionDevice(device: Device): ResultType<String>
    suspend fun getStreamUrl(serial: String): ResultType<URI>
    suspend fun getSnapshotUrl(serial: String): ResultType<URI>
    fun saveDevice(serial: String, canal:Int): Int
}