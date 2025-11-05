package com.team.data.local.notification

interface NotificationDataStore {
    suspend fun saveFcmToken(token: String)

    suspend fun saveFId(id: String)

    suspend fun getFcmToken(): String?

    suspend fun getFId(): String?

    suspend fun deletePushNotification()

    suspend fun allowedNotification(isAllowed: Boolean)

    suspend fun getIsNotificationAllowed(): Boolean?
}
