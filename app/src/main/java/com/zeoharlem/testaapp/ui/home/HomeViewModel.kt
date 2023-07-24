package com.zeoharlem.testaapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeoharlem.testaapp.models.Category
import com.zeoharlem.testaapp.repository.ApiServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val apiServices: ApiServices = ApiServices()
    private val _categoryStateFlow = MutableStateFlow(CategoryState())
    val categoryStateFlow = _categoryStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _categoryStateFlow.update {
                it.copy(
                    isLoading = true
                )
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