package com.team.todoktodok.data.datasource

import com.team.todoktodok.data.datasource.token.TokenDataSource

class FakeTokenLocalDataSource : TokenDataSource {
    override suspend fun getAccessToken(): String = "Bearer fake_access_token"

    override suspend fun getRefreshToken(): String = "Bearer fake_refresh_token"

    override suspend fun getMemberId(): Long = 0L

    override suspend fun saveAccessToken(accessToken: String) {}

    override suspend fun saveToken(
        accessToken: String,
        refreshToken: String,
    ) {}

    override suspend fun saveSetting(
        accessToken: String,
        refreshToken: String,
        memberId: Long,
    ) {
    }

    override suspend fun clear() {}
}
