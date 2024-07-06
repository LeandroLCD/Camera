package com.leandrolcd.presentation.device.states

sealed class DialogUiState {
    data object None:DialogUiState()
    data class Show(val url:String = "",
                    val user:String = "",
                    val password:String = "",
                    val onDismiss:()->Unit,
                    val onComplete:(url:String, user:String, password:String)->Unit): DialogUiState()

}