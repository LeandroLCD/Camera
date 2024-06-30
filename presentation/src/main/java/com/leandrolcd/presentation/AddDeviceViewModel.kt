package com.leandrolcd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.domain.models.DeviceInformation
import com.leandrolcd.domain.models.ResultTypeUiState
import com.leandrolcd.domain.useCase.ConnectionDeviceUseCase
import com.leandrolcd.domain.useCase.DiscoveryDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddDeviceViewModel(private val discoveryDevicesUseCase: DiscoveryDevicesUseCase,
    private val connectionDeviceUseCase: ConnectionDeviceUseCase):ViewModel() {

    private val _devicesList = MutableStateFlow<List<DeviceInformation>>(emptyList())
    val deviceList: StateFlow<List<DeviceInformation>>
        get() = _devicesList.asStateFlow()

    fun startDiscovery(){
        viewModelScope.launch {
            discoveryDevicesUseCase().collect{devices->
                _devicesList.update {
                    devices
                }
            }
        }
    }

    fun connectDevice(host: String, user:String, password:String){
        viewModelScope.launch{
            val result = connectionDeviceUseCase(host,user,password)
            when(result){
                is ResultTypeUiState.Error -> TODO()
                is ResultTypeUiState.Success -> {

                }
            }

        }

    }

}