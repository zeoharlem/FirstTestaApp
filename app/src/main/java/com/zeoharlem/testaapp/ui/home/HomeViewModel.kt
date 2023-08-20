package com.zeoharlem.testaapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeoharlem.testaapp.data.local.AppDataStore
import com.zeoharlem.testaapp.models.Category
import com.zeoharlem.testaapp.models.LoginRequest
import com.zeoharlem.testaapp.models.LoginResponse
import com.zeoharlem.testaapp.models.NetworkResults
import com.zeoharlem.testaapp.models.UiState
import com.zeoharlem.testaapp.repository.ApiServices
import com.zeoharlem.testaapp.repository.network.AuthRepository
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository,
    val cacheData: AppDataStore
) : ViewModel() {
    private val apiServices: ApiServices = ApiServices()
    private val _categoryStateFlow = MutableStateFlow(CategoryState())
    val categoryStateFlow = _categoryStateFlow.asStateFlow()

    private val _loginStateFlow = MutableStateFlow(UiState<LoginResponse>())
    val loginStateFlow = _loginStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _categoryStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
        }
    }

    fun loginUser(loginRequest: LoginRequest) = viewModelScope.launch {
        repository.loginUser(loginRequest).collectLatest { results ->
            when (results) {
                is NetworkResults.Failure -> {
                    _loginStateFlow.update {
                        it.copy(error = results.exception.localizedMessage)
                    }
                }

                is NetworkResults.Loading -> {
                    _loginStateFlow.update {
                        it.copy(isLoading = results.isLoading)
                    }
                }

                is NetworkResults.Success -> {
                    _loginStateFlow.update {
                        results.data?.token?.let { token -> cacheData.saveToken(token) }
                        results.data?.userData?.let { user -> cacheData.saveUserData(user) }
                        it.copy(data = results.data, error = null)
                    }
                }
            }
        }
    }

    fun categories() = viewModelScope.launch {
        apiServices.getCategories().onStart {
            _categoryStateFlow.update { categoryState ->
                categoryState.copy(
                    isLoading = true
                )
            }
        }.catch {
            _categoryStateFlow.update { categoryState ->
                categoryState.copy(
                    errorReport = it.localizedMessage?.toString()
                )
            }
        }.onCompletion {
            _categoryStateFlow.update { addressState ->
                addressState.copy(
                    isLoading = false
                )
            }
        }.collectLatest { list ->
            _categoryStateFlow.update { categoryState ->
                categoryState.copy(
                    addresses = list,
                    isLoading = false
                )
            }
        }
    }
}

data class CategoryState(
    val addresses: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorReport: String? = null
)