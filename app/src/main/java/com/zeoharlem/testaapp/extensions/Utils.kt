package com.zeoharlem.testaapp.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zeoharlem.testaapp.models.UserData

fun prettyGson(data: Any): String {
    return GsonBuilder().setPrettyPrinting().create().toJson(data)
}

fun<T> convertToObject(element: String, dataObject: (data: T?) -> T): T {
    return dataObject(Gson().fromJson(element, object : TypeToken<T>(){}.type))
}

fun convertToUserData(element: String): UserData {
    return Gson().fromJson(element, UserData::class.java)
}