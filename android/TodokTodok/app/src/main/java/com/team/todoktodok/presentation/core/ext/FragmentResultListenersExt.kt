package com.team.todoktodok.presentation.core.ext

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

fun FragmentManager.registerPositiveResultListener(
    lifecycleOwner: LifecycleOwner,
    requestKey: String,
    resultKey: String,
    onPositive: () -> Unit,
) {
    setFragmentResultListener(
        requestKey,
        lifecycleOwner,
    ) { _, bundle ->
        val result = bundle.getBoolean(resultKey)
        if (result) {
            onPositive()
        }
    }
}

fun FragmentManager.registerResultListener(
    lifecycleOwner: LifecycleOwner,
    requestKey: String,
    resultKey: String,
    onPositive: () -> Unit,
) {
    setFragmentResultListener(
        requestKey,
        lifecycleOwner,
    ) { _, bundle ->
        val result = bundle.getBoolean(resultKey)
        if (result) {
            onPositive()
        }
    }
}
