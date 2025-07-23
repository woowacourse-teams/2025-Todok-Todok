package com.example.todoktodok.data.di

import com.example.todoktodok.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkhttpModule {
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
            .addInterceptor(logger)
            .build()
}
