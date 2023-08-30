package com.zeoharlem.testaapp.repository.network

import com.zeoharlem.testaapp.models.NetworkResults
import com.zeoharlem.testaapp.repository.FirstTestaApiServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val apiServices: FirstTestaApiServices) {

    fun getCategories() = flow {
        emit(NetworkResults.Loading(true))
        emit(NetworkResults.Success(apiServices.getCategories()))
    }.catch { exception ->
        emit(NetworkResults.Failure(Exception(exception.localizedMessage)))
    }
}