package com.team.todoktodok.data.repository

import com.team.domain.repository.TokenRepository
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import javax.inject.Inject

class DefaultTokenRepository
    @Inject
    constructor(
        private val tokenLocalDataSource: TokenLocalDataSource,
    ) : TokenRepository {
        override suspend fun getMemberId(): Long = tokenLocalDataSource.getMemberId()

        override suspend fun logout() = tokenLocalDataSource.clear()
    }
