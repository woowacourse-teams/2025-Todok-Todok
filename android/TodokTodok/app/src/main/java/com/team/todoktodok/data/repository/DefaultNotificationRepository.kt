package com.team.todoktodok.data.repository

import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.data.datasource.notification.NotificationLocalDataSource
import com.team.todoktodok.data.datasource.notification.NotificationRemoteDataSource

class DefaultNotificationRepository(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
    private val notificationLocalDataSource: NotificationLocalDataSource,
) : NotificationRepository {
    override suspend fun registerPushNotification(): NetworkResult<Unit> {
        val fcmToken = notificationLocalDataSource.getFcmToken() ?: ""
        val fId = notificationLocalDataSource.getFId() ?: ""
        return notificationRemoteDataSource.saveFcmToken(fcmToken, fId)
    }

    override suspend fun registerPushNotification(
        token: String,
        fId: String,
    ): NetworkResult<Unit> {
        val storedFcmToken = notificationLocalDataSource.getFcmToken()
        val storedFcmFId = notificationLocalDataSource.getFId()

        val isNeedRegister =
            isNeedRegister(storedFcmToken, storedFcmFId, token, fId)
        if (isNeedRegister) {
            saveNewPushNotificationToLocal(token, fId)
            return notificationRemoteDataSource.saveFcmToken(token, fId)
        }
        return NetworkResult.Success(Unit)
    }

    private suspend fun saveNewPushNotificationToLocal(
        fcmToken: String,
        fId: String,
    ) {
        notificationLocalDataSource.saveFcmToken(fcmToken)
        notificationLocalDataSource.saveFId(fId)
    }

    private fun isNeedRegister(
        storedFcmToken: String?,
        storedFcmFId: String?,
        freshFcmToken: String,
        freshFcmFId: String,
    ): Boolean {
        if (storedFcmToken == null || storedFcmFId == null) return true
        if (storedFcmToken != freshFcmToken || storedFcmFId != freshFcmFId) return true
        return false
    }
}
