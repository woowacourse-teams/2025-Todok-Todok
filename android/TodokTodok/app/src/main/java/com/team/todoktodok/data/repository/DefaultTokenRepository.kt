package com.team.todoktodok.data.repository

import com.team.domain.repository.TokenRepository
import com.team.todoktodok.data.datasource.token.TokenDataSource

class DefaultTokenRepository(
    private val tokenDataSource: TokenDataSource,
) : TokenRepository {
    override suspend fun getMemberId(): Long = tokenDataSource.getMemberId()
}
