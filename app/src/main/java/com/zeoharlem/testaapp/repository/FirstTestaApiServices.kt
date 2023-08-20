package com.zeoharlem.testaapp.repository

import com.zeoharlem.testaapp.models.LoginRequest
import com.zeoharlem.testaapp.models.LoginResponse
import com.zeoharlem.testaapp.models.NetworkResults
import retrofit2.http.Body
import retrofit2.http.POST

interface FirstTestaApiServices {

    @POST("auth/signin")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
}