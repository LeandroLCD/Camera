package com.leandrolcd.camera.data.models

sealed class ResultType<T> {
    class Success<T>(val data: T) : ResultType<T>()
    class Error<T>( val message: String) : ResultType<T>()
}