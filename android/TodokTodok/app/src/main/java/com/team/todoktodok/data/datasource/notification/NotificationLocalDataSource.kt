package com.team.todoktodok.data.datasource.notification

interface NotificationLocalDataSource {
    suspend fun getFcmToken(): String?

    suspend fun getFId(): String?

    suspend fun saveFcmToken(token: String)

    suspend fun saveFId(id: String)
}
