package com.team.todoktodok.log

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class ReleaseCrashlyticsTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) return

        val firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        firebaseCrashlytics.log("[$tag] $message")
        t?.let { FirebaseCrashlytics.getInstance().recordException(it) }
    }
}
