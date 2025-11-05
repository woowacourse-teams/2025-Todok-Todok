package com.team.data.repository

import com.team.data.datasource.token.TokenLocalDataSource
import com.team.domain.repository.TokenRepository
import javax.inject.Inject

class DefaultTokenRepository
    @Inject
    constructor(
        private val tokenLocalDataSource: TokenLocalDataSource,
    ) : TokenRepository {
        override suspend fun getMemberId(): Long = tokenLocalDataSource.getMemberId()

        override suspend fun logout() = tokenLocalDataSource.clear()
    }
