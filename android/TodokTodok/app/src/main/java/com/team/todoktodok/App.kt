package com.team.todoktodok

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.team.todoktodok.log.ReleaseCrashlyticsTree
import timber.log.Timber

class App : Application() {
    val container: AppContainer by lazy {
        AppContainer(applicationContext)
    }

    override fun onCreate() {
        // 앱이 크래시 될 경우 자동으로 크래시틱스 전송 비활성화
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = BuildConfig.DEBUG.not()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
        } else {
            Timber.plant(ReleaseCrashlyticsTree())
        }
    }
}
