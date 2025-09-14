package com.team.todoktodok.data.datasource.notification

import com.team.todoktodok.data.local.nofitication.NotificationEntity

interface NotificationLocalDataSource {
    suspend fun getFcmToken(): String?

    suspend fun getFId(): String?

    suspend fun saveFcmToken(token: String)

    suspend fun saveFId(id: String)

    suspend fun deletePushNotification()

    suspend fun getNotifications(): List<NotificationEntity>

    suspend fun saveNotifications(notificationEntity: NotificationEntity): Long

    suspend fun getNotificationCount(): Int
}
