package com.leandrolcd.domain.models

sealed class ResultTypeUiState <T> {
    class Success<T>(val data: T) : ResultTypeUiState<T>()
    class Error<T>( val message: String) : ResultTypeUiState<T>()
}