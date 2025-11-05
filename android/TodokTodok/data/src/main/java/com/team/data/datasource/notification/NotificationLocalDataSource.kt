package com.team.data.datasource.notification

interface NotificationLocalDataSource {
    suspend fun getFcmToken(): String?

    suspend fun getFId(): String?

    suspend fun saveFcmToken(token: String)

    suspend fun saveFId(id: String)

    suspend fun allowedNotification(isAllowed: Boolean)

    suspend fun getIsNotificationAllowed(): Boolean?
}
