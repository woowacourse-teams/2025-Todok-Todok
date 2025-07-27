package com.team.todoktodok.data.di

import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.network.auth.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkhttpModule(
    authInterceptor: AuthInterceptor,
) {
    private val logger =
        HttpLoggingInterceptor().let {
            if (BuildConfig.DEBUG) {
                it.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                it.setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        }

    val instance =
        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()
}
