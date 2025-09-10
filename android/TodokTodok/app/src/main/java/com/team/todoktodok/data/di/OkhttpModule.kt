package com.team.todoktodok.data.di

import com.team.todoktodok.BuildConfig
import com.team.todoktodok.data.core.PrettyJsonLogger
import com.team.todoktodok.data.network.auth.AuthInterceptor
import com.team.todoktodok.data.network.auth.TokenAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkhttpModule(
    tokenAuthenticator: TokenAuthenticator,
    authInterceptor: AuthInterceptor,
) {
    private val logger =
        HttpLoggingInterceptor(PrettyJsonLogger()).let {
            if (BuildConfig.DEBUG) {
                it.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                it.setLevel(HttpLoggingInterceptor.Level.NONE)
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
