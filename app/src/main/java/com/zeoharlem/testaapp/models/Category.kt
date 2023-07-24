package com.zeoharlem.testaapp.models

data class Category(
    val categoryId: Int,
    val categoryTitle: String,
    val urlImage: String? = null,
    val description: String? = null
)