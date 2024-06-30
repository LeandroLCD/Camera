package com.leandrolcd.domain.useCase

import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.models.ResultTypeUiState
import javax.inject.Inject

class ConnectionDeviceUseCase @Inject constructor(private val repository: IOnvifDeviceRepository) {

    private val regex = Regex("((\\d{1,3}\\.){3}\\d{1,3})(:\\d+)?")

    suspend operator fun invoke(device: Device): ResultTypeUiState<String> {
        return repository.connectionDevice(device)
    }

    suspend operator fun invoke(url:String, user:String = "admin", password:String = ""): ResultTypeUiState<String> {
        return if(url.isBlank() && regex.matches(url)){
            ResultTypeUiState.Error("Url invalid.")
        }else{
            repository.connectionDevice(url, user, password)
        }
    }
}