package com.team.domain.repository

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun registerPushNotification(): NetworkResult<Unit>

    suspend fun registerPushNotification(
        token: String,
        fId: String,
    ): NetworkResult<Unit>

    suspend fun getNotifications(): NetworkResult<Pair<Int, List<Notification>>>

    suspend fun getUnreadNotificationsCount(): NetworkResult<Boolean>

    suspend fun deleteNotification(notificationId: Long): NetworkResult<Unit>

    suspend fun readNotification(notificationId: Long): NetworkResult<Unit>

    suspend fun allowedNotification(isAllowed: Boolean)

    suspend fun getIsNotificationAllowed(): Boolean?

}
