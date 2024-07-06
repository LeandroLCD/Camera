package com.leandrolcd.presentation.device

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandrolcd.domain.models.Device
import com.leandrolcd.domain.useCase.ConnectionDeviceUseCase
import com.leandrolcd.domain.useCase.GetDeviceByIdUseCase
import com.leandrolcd.domain.useCase.GetStreamUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneCameraViewModel @Inject constructor(
    private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
    private val connectionDeviceUseCase: ConnectionDeviceUseCase,
    private val getStreamUrlUseCase: GetStreamUrlUseCase
) : ViewModel() {
    private val _id = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val device = _id.flatMapLatest() {
        if(it==0){
            flowOf(null)
        }else{
            getDeviceByIdUseCase.invoke(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), null)

    private val _urlStream = MutableStateFlow("")
    val urlStream = _urlStream.asStateFlow()

    fun load(deviceId: Int) {
        _id.update { deviceId }
    }

    fun connectDevice(device: Device){
        viewModelScope.launch {
            Log.d("CameraViewModel", "connectDevice: $device")
            connectionDeviceUseCase(device).onSuccess {
                Log.d("CameraViewModel", "connectDevice: $it")
                getStreamUrlUseCase.invoke(it).onSuccess { url->
                    Log.d("CameraViewModel", "connectDevice: $url")
                    _urlStream.tryEmit(url.toString())
                }.onFailure {
                    Log.d("CameraViewModel", "onFailure: $it")

                }
            }
        }
    }


}