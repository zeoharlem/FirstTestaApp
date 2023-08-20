package com.zeoharlem.testaapp.models

data class UiState<out T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val hasError get() = !error.isNullOrEmpty()
    val errorOrEmpty get() = error ?: ""
}