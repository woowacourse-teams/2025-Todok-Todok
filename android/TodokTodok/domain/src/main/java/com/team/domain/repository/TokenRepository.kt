package com.team.domain.repository

interface TokenRepository {
    suspend fun getMemberId(): Long

    suspend fun logout()
}
