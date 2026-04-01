package com.leandrolcd.presentation.device.states

import android.os.Message

sealed class AddDeviceUiState {
    data object Loaded: AddDeviceUiState()
    data class Discovering(val count:Int = 0): AddDeviceUiState()
    data class Error (val message: String): AddDeviceUiState()
}