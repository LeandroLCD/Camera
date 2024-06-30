package com.leandrolcd.camera.domain.useCase

import com.leandrolcd.camera.data.models.ResultType
import com.leandrolcd.camera.domain.models.Device
import javax.inject.Inject

class ConnectionDeviceUseCase @Inject constructor(private val repository:IOnvifDeviceRepository) {

    private val regex = Regex("((\\d{1,3}\\.){3}\\d{1,3})(:\\d+)?")

    suspend operator fun invoke(device: Device): ResultType<String> {
        return repository.connectionDevice(device)
    }

    suspend operator fun invoke(url:String, user:String = "admin", password:String = ""): ResultType<String> {
        return if(url.isBlank() && regex.matches(url)){
            ResultType.Error("Url invalid.")
        }else{
            repository.connectionDevice(url, user, password)
        }
    }
}