package com.zeoharlem.testaapp.models

data class CartMenu<out T>(
    val dataType: T,
    var quantity: Int
)