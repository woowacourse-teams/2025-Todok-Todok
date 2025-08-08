package com.team.todoktodok.presentation.core.ext

import android.content.Intent
import android.os.Build
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.getParcelableCompat(key: String): T {
    val value =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(key) as? T
        }

    return requireNotNull(value) {
        "Bundle extra '$key' is missing or not of expected type ${T::class.java.name}"
    }
}