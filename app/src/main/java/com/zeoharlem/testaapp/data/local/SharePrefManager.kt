package com.zeoharlem.testaapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

const val PREF_NAME = "first_testa"

open class SharePrefManager(context: Context) {
    private val pref: SharedPreferences =
        context.getSharedPreferences("${PREF_NAME}_SharedPreferences", Context.MODE_PRIVATE)


    /* Save
    ------------------------------------------------------------------------ */

    fun saveStringData(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun saveBooleanData(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    fun saveDoubleData(key: String, value: Double) {
        pref.edit().putFloat(key, value.toFloat()).apply()
    }

    fun saveIntData(key: String, value: Int) {
        pref.edit().putInt(key, value).apply()
    }

    fun saveLongData(key: String, value: Long) {
        pref.edit().putLong(key, value).apply()
    }


    /* Get
    ------------------------------------------------------------------------ */

    fun getBooleanData(key: String, default: Boolean = false): Boolean {
        return pref.getBoolean(key, default)
    }

    fun getIntData(key: String, default: Int = 0): Int {
        return pref.getInt(key, default)
    }

    fun getFloatData(key: String, default: Float = 0F): Float {
        return pref.getFloat(key, default)
    }

    fun getDoubleData(key: String, default: Double = 0.0): Double {
        return pref.getFloat(key, default.toFloat()).toDouble()
    }

    fun getLongData(key: String, default: Long = 0L): Long {
        return pref.getLong(key, default)
    }

    fun getStringData(key: String, default: String = ""): String {
        return pref.getString(key, default) ?: default
    }

    fun <T> saveObject(key: String, obj : T){
        val jsonStr = Gson().toJson(obj)
        saveStringData(key, jsonStr)
    }

    inline fun <reified T> getObject(key: String): T?{
        val jsonStr = getStringData(key)
        return Gson().fromJson(jsonStr, T::class.java)
    }

    private fun delete(key: String) {
        if (pref.contains(key)) {
            pref.edit().remove(key).apply()
        }
    }

    fun clearData(key: String) = delete(key)
}