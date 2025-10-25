package com.team.todoktodok.di

import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.network.auth.AuthInterceptor
import com.team.todoktodok.log.PrettyJsonLogger
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkhttpModule(
    tokenAuthenticator: Authenticator,
    authInterceptor: AuthInterceptor,
) {
    private val logger =
        HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                setLevel(HttpLoggingInterceptor.Level.NONE)
            }
        }

    val authClient =
        OkHttpClient
            .Builder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()

    val client =
        OkHttpClient
            .Builder()
            .addInterceptor(logger)
            .build()
}
