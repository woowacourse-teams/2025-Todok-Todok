package com.team.todoktodok.data.network.auth

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.core.ext.extractTokens
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.request.RefreshRequest
import com.team.todoktodok.data.network.service.RefreshService

class TokenRefreshDelegator(
    private val refreshService: RefreshService,
    private val tokenDataSource: TokenDataSource,
) : TokenRefreshDelegate {
    override suspend fun refresh(): String? {
        val response = refreshService.refresh(
            RefreshRequest(tokenDataSource.getRefreshToken()),
        )

        return when (val result = response.extractTokens { accessToken, refreshToken ->
            tokenDataSource.saveToken(accessToken, refreshToken)
            accessToken
        }) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Failure -> null
        }
    }
}
