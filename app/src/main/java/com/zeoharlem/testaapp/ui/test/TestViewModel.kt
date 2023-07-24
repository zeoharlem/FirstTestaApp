package com.zeoharlem.testaapp.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeoharlem.testaapp.data.CartRepository
import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Tests
import com.zeoharlem.testaapp.repository.ApiServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestViewModel: ViewModel() {

    private val apiServices: ApiServices = ApiServices()
    private val cartRepository: CartRepository = CartRepository()
    private val _testStateFlow = MutableStateFlow(TestsState())
    val testStateFlow = _testStateFlow.asStateFlow()


    init {
        viewModelScope.launch {
            _testStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
        }
    }

    fun testsList() = viewModelScope.launch {
        apiServices.getTestList().onStart {
            _testStateFlow.update { testsState ->
                testsState.copy(
                    isLoading = true
                )
            }
        }.catch {
            _testStateFlow.update { testsState ->
                testsState.copy(
                    errorReport = it.localizedMessage?.toString()
                )
            }
        }.onCompletion {
            _testStateFlow.update { testsState ->
                testsState.copy(
                    isLoading = false
                )
            }
        }.collectLatest { list ->
            _testStateFlow.update { testsState ->
                testsState.copy(
                    testsList = list,
                    isLoading = false
                )
            }
        }
    }

    fun addItem(tests: Tests) {
        cartRepository.addItem(tests)
    }

    fun removeItem(cartMenu: CartMenu<Tests>, quantity: Int = 0){
        cartRepository.removeItem(cartMenu, quantity)
    }

    fun getCartMenuItems(): MutableLiveData<List<CartMenu<Tests>>> {
        return cartRepository.mutableCart
    }

    fun updateItems(cartMenu: CartMenu<Tests>, quantity: Int) {
        cartRepository.updateQuantity(cartMenu, quantity)
    }

    fun getTotalPrice(): LiveData<Double> {
        return cartRepository.getTotalPrice()
    }
}

data class TestsState(
    val testsList: List<CartMenu<Tests>> = emptyList(),
    val isLoading: Boolean = false,
    val errorReport: String? = null
)