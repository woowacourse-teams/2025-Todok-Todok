package com.team.data.datasource.notification

import com.team.data.network.request.FcmTokenRequest
import com.team.data.network.response.notification.ExistNotificationResponse
import com.team.data.network.response.notification.NotificationResponse
import com.team.data.network.service.NotificationService
import com.team.domain.model.exception.NetworkResult
import javax.inject.Inject

class DefaultNotificationRemoteDataSource
    @Inject
    constructor(
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
