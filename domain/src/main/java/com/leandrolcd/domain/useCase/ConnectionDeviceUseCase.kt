package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import javax.inject.Inject

class ConnectionDeviceUseCase @Inject constructor(private val repository: IOnvifDeviceRepository) {

    suspend operator fun invoke(device: Device): Result<String> {
        return repository.connectionDevice(device)
    }

    suspend operator fun invoke(url:String, user:String = "admin", password:String = ""): Result<String> {
        return repository.connectionDevice(url, user, password)
    }
}