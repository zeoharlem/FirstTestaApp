package com.zeoharlem.testaapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeoharlem.testaapp.data.local.AppDataStore
import com.zeoharlem.testaapp.models.Category
import com.zeoharlem.testaapp.models.NetworkResults
import com.zeoharlem.testaapp.models.UiState
import com.zeoharlem.testaapp.repository.ApiServices
import com.zeoharlem.testaapp.repository.network.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    val cacheData: AppDataStore
) : ViewModel() {
    private val apiServices: ApiServices = ApiServices()
    private val _categoryStateFlow = MutableStateFlow(UiState<List<Category>>())
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
        categoryRepository.getCategories().collectLatest { results ->
            when (results) {
                is NetworkResults.Loading -> {
                    _categoryStateFlow.update {
                        it.copy(isLoading = results.isLoading)
                    }
                }
                is NetworkResults.Failure -> {
                    _categoryStateFlow.update {
                        it.copy(error = results.exception.localizedMessage)
                    }
                }
                is NetworkResults.Success -> {
                    _categoryStateFlow.update {
                        it.copy(
                            data = results.data,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}