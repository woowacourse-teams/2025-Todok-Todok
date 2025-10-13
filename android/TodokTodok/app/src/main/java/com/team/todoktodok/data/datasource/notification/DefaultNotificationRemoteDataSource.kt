package com.team.todoktodok.data.datasource.notification

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.FcmTokenRequest
import com.team.todoktodok.data.network.response.notification.ExistNotificationResponse
import com.team.todoktodok.data.network.response.notification.NotificationResponse
import com.team.todoktodok.data.network.service.NotificationService

class DefaultNotificationRemoteDataSource(
    private val notificationService: NotificationService,
) : NotificationRemoteDataSource {
    override suspend fun saveFcmToken(
        fcmToken: String,
        fId: String,
    ): NetworkResult<Unit> {
        val fcmTokenRequest = FcmTokenRequest(fcmToken, fId)
        return notificationService.saveFcmToken(fcmTokenRequest)
    }

    override suspend fun getNotification(): NetworkResult<NotificationResponse> = notificationService.getNotifications()

    override suspend fun getUnreadNotificationsCount(): NetworkResult<ExistNotificationResponse> =
        notificationService.getUnreadNotificationsCount()

    override suspend fun deleteNotification(notificationId: Long): NetworkResult<Unit> =
        notificationService.deleteNotifications(notificationId = notificationId)

    override suspend fun readNotification(notificationId: Long): NetworkResult<Unit> =
        notificationService.patchNotifications(notificationId = notificationId)
}
