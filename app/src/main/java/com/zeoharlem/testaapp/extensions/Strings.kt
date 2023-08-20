package com.zeoharlem.testaapp.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.text.NumberFormat

fun String.toTitleCase(): String {
    return replaceFirstChar { it.uppercase() }
}

fun String.toWordTitleCase(separator: String = " "): String {
    return split("\\s+".toRegex()).joinToString(separator) {
        it.toTitleCase()
    }
}

fun String.toLocalPrice(): String {
    return NumberFormat.getCurrencyInstance().format(this.toInt())
}

fun<T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}