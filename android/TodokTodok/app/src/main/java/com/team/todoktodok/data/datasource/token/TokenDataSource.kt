package com.team.todoktodok.data.datasource.token

interface TokenDataSource {
    suspend fun getAccessToken(): String

    suspend fun getRefreshToken(): String

    suspend fun getMemberId(): Long

    suspend fun saveToken(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun saveSetting(
        accessToken: String,
        refreshToken: String,
        memberId: Long = TEMPORARY_MEMBER_ID,
    )

    companion object {
        private const val TEMPORARY_MEMBER_ID = 0L
    }
}
