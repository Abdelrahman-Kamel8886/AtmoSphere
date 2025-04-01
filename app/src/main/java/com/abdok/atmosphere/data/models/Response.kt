package com.abdok.atmosphere.data.models

sealed class Response<out T>{
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: String) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}