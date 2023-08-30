package com.zeoharlem.testaapp.common

import android.annotation.SuppressLint
import android.util.Log

internal const val appLogTag = "FirstTesta"

@SuppressLint("LogNotTimber")
internal object Logg {


    fun debug(msg: String?, t: Throwable? = null) {
        Log.d(appLogTag, msg ?: "", t)
    }

    fun error(msg: String?, t: Throwable? = null) {
        Log.e(appLogTag, msg, t)
    }

    fun errorDebug(msg: String?, t: Throwable? = null) {
        Log.e(appLogTag, msg, t)
        Log.d(appLogTag, "[ERROR!!!]  $msg", t)
    }

}