package com.team.data.datasource.token

interface TokenDataSource {
    suspend fun getAccessToken(): String

    suspend fun getRefreshToken(): String

    suspend fun getMemberId(): Long

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveToken(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun saveSetting(
        accessToken: String,
        refreshToken: String,
        memberId: Long = TEMPORARY_MEMBER_ID,
    )

    suspend fun clear()

    companion object {
        private const val TEMPORARY_MEMBER_ID = 0L
    }
}
