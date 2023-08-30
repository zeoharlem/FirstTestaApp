package com.zeoharlem.testaapp.repository

import com.zeoharlem.testaapp.models.CartMenu
import com.zeoharlem.testaapp.models.Category
import com.zeoharlem.testaapp.models.Tests
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiServices {

    fun getCategories(): Flow<List<Category>> = flow {
        val categoryList = mutableListOf(
            Category(1, "infections"),
            Category(2, "heart health"),
            Category(3, "malaria"),
            Category(4, "typhoid"),
            Category(5, "urinalysis"),
            Category(6, "blood test"),
        )
        emit(categoryList)
        delay(2000)
    }

    fun getTestList(): Flow<List<CartMenu<Tests>>> = flow {
        val lists = mutableListOf(
            CartMenu(Tests(1, "infections", "2500"), 0),
            CartMenu(Tests(2, "heart health", "2600"), 0),
            CartMenu(Tests(3, "malaria", "3200"), 0),
            CartMenu(Tests(4, "typhoid", "1200"), 0),
            CartMenu(Tests(5, "urinalysis", "2100"), 0),
            CartMenu(Tests(6, "blood test", "1500"), 0),
        )
        emit(lists)
        delay(2000)
    }
}