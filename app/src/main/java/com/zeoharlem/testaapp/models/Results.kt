package com.zeoharlem.testaapp.models

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Loading(val isLoading: Boolean = true) : Result<Nothing>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}
