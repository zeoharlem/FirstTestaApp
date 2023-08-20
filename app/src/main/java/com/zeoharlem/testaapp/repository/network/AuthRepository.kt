package com.zeoharlem.testaapp.repository.network

import com.zeoharlem.testaapp.models.LoginRequest
import com.zeoharlem.testaapp.models.NetworkResults
import com.zeoharlem.testaapp.repository.FirstTestaApiServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiServices: FirstTestaApiServices) {

    suspend fun loginUser(loginRequest: LoginRequest) = flow {
        emit(NetworkResults.Loading(true))
        emit(NetworkResults.Success(apiServices.loginUser(loginRequest)))
    }.catch { exception ->
        println("AuthRepository Error: ${exception.localizedMessage}")
        emit(NetworkResults.Failure(Exception(exception.localizedMessage)))
    }
}