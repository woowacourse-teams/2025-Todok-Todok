package com.team.todoktodok.data.network.auth

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.core.ext.extractTokens
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.request.RefreshRequest
import com.team.todoktodok.data.network.service.RefreshService
import javax.inject.Inject

class TokenRefreshDelegator
    @Inject
    constructor(
        private val refreshService: RefreshService,
        private val tokenLocalDataSource: TokenDataSource,
    ) {
        suspend fun refresh(): String? {
            val response =
                refreshService.refresh(
                    RefreshRequest(tokenLocalDataSource.getRefreshToken()),
                )

            return when (
                val result =
                    response.extractTokens { accessToken, refreshToken ->
                        requireNotNull(refreshToken) { TodokTodokExceptions.RefreshTokenNotReceivedException }
                        tokenLocalDataSource.saveToken(accessToken, refreshToken)
                        accessToken
                    }
            ) {
                is NetworkResult.Success -> result.data
                is NetworkResult.Failure -> null
            }
        }
    }
