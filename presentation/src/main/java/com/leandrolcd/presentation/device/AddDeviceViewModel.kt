package com.leandrolcd.presentation.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.domain.models.ResultTypeUiState
import com.leandrolcd.domain.useCase.ConnectionDeviceUseCase
import com.leandrolcd.domain.useCase.DiscoveryDevicesUseCase
import com.leandrolcd.domain.useCase.SaveDeviceUseCase
import com.leandrolcd.presentation.device.mapper.MapperDomain
import com.leandrolcd.presentation.device.model.DeviceInfo
import com.leandrolcd.presentation.device.states.AddDeviceUiState
import com.leandrolcd.presentation.device.states.DialogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDeviceViewModel @Inject constructor(private val discoveryDevicesUseCase: DiscoveryDevicesUseCase,
    private val connectionDeviceUseCase: ConnectionDeviceUseCase,
    private val saveDeviceUseCase: SaveDeviceUseCase,
    private val mapperDomain: MapperDomain):ViewModel() {

    private val _deviceList = MutableStateFlow<List<DeviceInfo>>(emptyList())
    val deviceList: StateFlow<List<DeviceInfo>>
        get() = _deviceList.asStateFlow()


    private val _dialogUiState = MutableStateFlow<DialogUiState>(DialogUiState.None)

    val dialogUiState: StateFlow<DialogUiState>
        get() = _dialogUiState.asStateFlow()

    private val _uiState = MutableStateFlow<AddDeviceUiState>(AddDeviceUiState.Loaded)

    val uiState: StateFlow<AddDeviceUiState>
        get() = _uiState.asStateFlow()

    private var jobDiscovery: Job? = null

    fun startDiscovery(){
        _uiState.update {
            AddDeviceUiState.Discovering()
        }
        jobDiscovery = viewModelScope.launch {
            discoveryDevicesUseCase().collect{devices->
                val count = devices.size
                _uiState.update {
                    AddDeviceUiState.Discovering(count)
                }
                _deviceList.update {
                    devices.map {d->
                        mapperDomain.toModel(d)
                    }
                }
            }
        }
        jobDiscovery?.invokeOnCompletion {
            _uiState.update {
                AddDeviceUiState.Loaded
            }
        }
    }
    fun retry(){
        _uiState.update {
            AddDeviceUiState.Loaded
        }
    }

    fun stopDiscovery(){
        jobDiscovery?.cancel( )
        _uiState.update {
            AddDeviceUiState.Loaded
        }
    }

    fun connectDevice(host: String, user:String, password:String){
        viewModelScope.launch{
            when(val result = connectionDeviceUseCase(host,user,password)){
                is ResultTypeUiState.Error -> {
                    _uiState.update {
                        AddDeviceUiState.Error(result.message)
                    }
                }
                is ResultTypeUiState.Success -> {
                    _dialogUiState.value = DialogUiState.None
                    saveDeviceUseCase.invoke(result.toString(), 0)

                }
            }
        }
    }

    fun showDialogAddDevice(device:DeviceInfo){
        _dialogUiState.update{
            DialogUiState.Show(url = device.host, user = device.friendlyName.orEmpty(), onDismiss = {
                _dialogUiState.value = DialogUiState.None
            }){host, user, pass->
                connectDevice(host, user, pass)
            }
        }
    }
    fun showDialogAddDevice(){
        _dialogUiState.update{
            DialogUiState.Show(onDismiss = {
                _dialogUiState.value = DialogUiState.None
            }){host, user, pass->
                connectDevice(host, user, pass)
            }
        }
    }

}