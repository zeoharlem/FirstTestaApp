package com.zeoharlem.testaapp.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Tests

class CartRepository() {
    private var _menuCartItem = MutableLiveData<CartMenu<Tests>>()
    val menuListItem get() = _menuCartItem

    private val _mutableCartMenu = MutableLiveData<CartMenu<Tests>>()
    val mutableCartMenu get() = _mutableCartMenu

    private val _mutableCart = MutableLiveData<List<CartMenu<Tests>>>()
    val mutableCart get() = _mutableCart

    private val _mutableTotalPrice = MutableLiveData<Double>()
    val mutableTotalPrice get() = _mutableTotalPrice

    private val _mutableSingleCart = MutableLiveData<CartMenu<Tests>>()
    val mutableSingleCart get() = _mutableSingleCart

    init {
        _mutableCart.value = arrayListOf()
        totalPriceCalculated()
    }

    fun addItem(testMenu: Tests) {
        val listItems = ArrayList<CartMenu<Tests>>(_mutableCart.value)
        val cartItem = CartMenu(testMenu, 1)
        listItems.forEach { if (it.dataType.testId == testMenu.testId) return }
        listItems.add(cartItem)
        _mutableCart.value = listItems
        totalPriceCalculated()
    }

    fun removeItem(cartMenu: CartMenu<Tests>, quantity: Int = 0) {
        if (_mutableCart.value.isNullOrEmpty()) return
        updateQuantity(cartMenu, quantity)
        ArrayList<CartMenu<Tests>>(_mutableCart.value).let { cartItems ->
            cartItems.removeIf { it.dataType.testId == cartMenu.dataType.testId }
            _mutableCart.value = cartItems
        }
        totalPriceCalculated()
    }

    fun updateQuantity(cartMenu: CartMenu<Tests>, quantity: Int) {
        ArrayList<CartMenu<Tests>>(_mutableCart.value).let {
            it.add(CartMenu(cartMenu.dataType, quantity))
            _mutableCart.value = it
        }
    }

    private fun totalPriceCalculated() {
        var total = 0.0
        _mutableCart.value?.let {
            for (cartItem in it) {
                total += cartItem.dataType.price.toDouble() * cartItem.quantity
            }
        }
        _mutableTotalPrice.value = total
    }

    fun getTotalPrice(): LiveData<Double> {
        if (_mutableTotalPrice.value == null) _mutableTotalPrice.value = 0.0
        return _mutableTotalPrice
    }
}