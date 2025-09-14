package com.team.todoktodok.data.datasource.notification

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.FcmTokenRequest
import com.team.todoktodok.data.network.service.NotificationService

class DefaultNotificationRemoteDataSource(
    private val notificationService: NotificationService,
) : NotificationRemoteDataSource {
    override suspend fun saveFcmToken(
        fcmToken: String,
        fId: String,
    ): NetworkResult<Unit> {
        FcmTokenRequest(fcmToken, fId)
//        return notificationService.saveFcmToken(fcmTokenRequest)
        return NetworkResult.Success(Unit)
    }
}
