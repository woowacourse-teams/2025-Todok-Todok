package com.team.domain.repository

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun registerPushNotification(
        token: String,
        fId: String,
    ): NetworkResult<Unit>

    suspend fun deletePushNotification()

    suspend fun getNotifications(): List<Notification>

    suspend fun saveNotification(notification: Notification): Long

    suspend fun getNotificationCount(): Int
}
