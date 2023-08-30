package com.zeoharlem.testaapp.repository

import com.zeoharlem.testaapp.models.Category
import com.zeoharlem.testaapp.models.LoginRequest
import com.zeoharlem.testaapp.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FirstTestaApiServices {

    @POST("auth/signin")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @GET("category")
    suspend fun getCategories(): List<Category>
}