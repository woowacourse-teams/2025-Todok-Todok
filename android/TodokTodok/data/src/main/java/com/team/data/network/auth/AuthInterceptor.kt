package com.team.data.network.auth

import com.team.data.core.AuthorizationConstants
import com.team.data.datasource.token.TokenLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
    @Inject
    constructor(
        private val tokenLocalDataSource: TokenLocalDataSource,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val builder = request.newBuilder()
            val accessToken = runBlocking { tokenLocalDataSource.getAccessToken() }
            builder.addHeader(AuthorizationConstants.HEADER_AUTHORIZATION, accessToken)

            return chain.proceed(builder.build())
        }
    }
