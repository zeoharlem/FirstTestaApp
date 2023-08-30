package com.zeoharlem.testaapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zeoharlem.testaapp.extensions.toWordTitleCase
import kotlinx.parcelize.Parcelize


data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)

data class SignUpResponse(
    val accessToken: String
)

data class LoginRequest(
    var email: String,
    var password: String
)

@Parcelize
data class LoginResponse(
    val code: Int = 0,
    @SerializedName("accessToken")
    var token: String = "",
    @SerializedName("user")
    var userData: UserData? = null,
): Parcelable

@Parcelize
data class Category(
    @SerializedName("id")
    val categoryId: Int,
    @SerializedName("title")
    val categoryTitle: String,
    @SerializedName("url_image")
    val urlImage: String? = null,
    @SerializedName("content_desc")
    val description: String? = null,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("code")
    val code: String = "",
) : Parcelable

@Parcelize
data class UserData(
    @SerializedName("email")
    var email: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("firstname")
    var firstName: String,
    @SerializedName("lastname")
    var lastName: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("createdAt")
    val createdAt: String
): Parcelable {
    fun getFullName(): String {
        return "$firstName $lastName".toWordTitleCase()
    }
}