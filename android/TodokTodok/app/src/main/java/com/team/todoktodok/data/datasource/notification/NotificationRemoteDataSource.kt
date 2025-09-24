package com.team.todoktodok.data.datasource.notification

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.response.notification.ExistNotificationResponse
import com.team.todoktodok.data.network.response.notification.NotificationResponse

interface NotificationRemoteDataSource {
    suspend fun saveFcmToken(
        fcmToken: String,
        fId: String,
    ): NetworkResult<Unit>

    suspend fun getNotification(
    ): NetworkResult<NotificationResponse>

    suspend fun getUnreadNotificationsCount(): NetworkResult<ExistNotificationResponse>

    suspend fun deleteNotification(
        notificationId: Long
    ): NetworkResult<Unit>

    suspend fun readNotification(
        notificationId: Long
    ): NetworkResult<Unit>
}
