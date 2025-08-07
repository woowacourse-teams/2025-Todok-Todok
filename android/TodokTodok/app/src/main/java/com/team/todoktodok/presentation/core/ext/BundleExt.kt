package com.team.todoktodok.presentation.core.ext

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.jvm.java

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompat(key: String): ArrayList<T> {
    val value =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableArrayList(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableArrayList(key)
        }

    return requireNotNull(value) {
        "Bundle extra '$key' is missing or not of expected type ArrayList<${T::class.java.name}>"
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T {
    val value =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelable(key)
        }

    return requireNotNull(value) {
        "Bundle extra '$key' is missing or not of expected type ${T::class.java.name}"
    }
}

inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String): T {
    val value =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getSerializable(key) as? T
        }

    return requireNotNull(value) {
        "Bundle extra '$key' is missing or not of expected type ${T::class.java.name}"
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableCompat(key: String): T {
    val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(key) as? T
    }

    return requireNotNull(value) {
        "Bundle extra '$key' is missing or not of expected type ${T::class.java.name}"
    }
}