package com.team.todoktodok.data.network.auth

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.core.AuthorizationConstants
import com.team.todoktodok.data.core.ext.extractTokens
import com.team.todoktodok.data.core.ext.retryAttemptCount
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.request.RefreshRequest
import com.team.todoktodok.data.network.service.RefreshService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator2
    @Inject
    constructor(
        private val service: RefreshService,
        private val tokenDataSource: TokenDataSource,
    ) : Authenticator {
        private val mutex = Mutex()

        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (response.retryAttemptCount() >= MAX_ATTEMPT_COUNT) return null

            return runBlocking {
                mutex.withLock {
                    val newAccessToken = refreshToken()
                    buildRequestWithToken(response.request, newAccessToken)
                }
            }
        }

        suspend fun refreshToken(): String? {
            val response =
                service.refresh(
                    RefreshRequest(tokenDataSource.getRefreshToken()),
                )

            return when (
                val result =
                    response.extractTokens { accessToken, refreshToken ->
                        requireNotNull(refreshToken) { TodokTodokExceptions.RefreshTokenNotReceivedException }
                        tokenDataSource.saveToken(accessToken, refreshToken)
                        accessToken
                    }
            ) {
                is NetworkResult.Success -> result.data
                is NetworkResult.Failure -> null
            }
        }

        private fun buildRequestWithToken(
            originalRequest: Request,
            newAccessToken: String?,
        ): Request? {
            if (newAccessToken.isNullOrBlank()) return null
            return originalRequest
                .newBuilder()
                .header(
                    AuthorizationConstants.HEADER_AUTHORIZATION,
                    AuthorizationConstants.HEADER_AUTHORIZATION_TYPE.format(newAccessToken),
                ).build()
        }

        companion object {
            private const val MAX_ATTEMPT_COUNT = 3
        }
    }
