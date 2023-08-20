package com.zeoharlem.testaapp.models

sealed class NetworkResults<out T> {
    data class Success<T>(val data: T?) : NetworkResults<T>()
    data class Loading(val isLoading: Boolean = true) : NetworkResults<Nothing>()
    data class Failure(val exception: Exception) : NetworkResults<Nothing>()
}
