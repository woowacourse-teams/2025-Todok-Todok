package com.example.todoktodok.data.network.auth

import com.example.todoktodok.data.datasource.token.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenDataSource: TokenDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val accessToken = runBlocking { tokenDataSource.getAccessToken() }

        builder.addHeader(AUTHORIZATION_NAME, accessToken)

        return chain.proceed(builder.build())
    }

    companion object {
        const val AUTHORIZATION_NAME = "Authorization"
    }
}
