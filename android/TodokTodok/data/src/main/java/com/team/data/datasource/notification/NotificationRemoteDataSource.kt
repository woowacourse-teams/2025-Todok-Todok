package com.team.data.datasource.notification

import com.team.data.network.response.notification.ExistNotificationResponse
import com.team.data.network.response.notification.NotificationResponse
import com.team.domain.model.exception.NetworkResult

interface NotificationRemoteDataSource {
    suspend fun saveFcmToken(
        fcmToken: String,
        fId: String,
    ): NetworkResult<Unit>

    suspend fun getNotification(): NetworkResult<NotificationResponse>

    suspend fun getUnreadNotificationsCount(): NetworkResult<ExistNotificationResponse>

    suspend fun deleteNotification(notificationId: Long): NetworkResult<Unit>

    suspend fun readNotification(notificationId: Long): NetworkResult<Unit>
}
