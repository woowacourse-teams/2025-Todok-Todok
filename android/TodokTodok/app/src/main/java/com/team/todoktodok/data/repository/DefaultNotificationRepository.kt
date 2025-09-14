package com.team.todoktodok.data.repository

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.notification.Notification
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.data.datasource.notification.NotificationLocalDataSource
import com.team.todoktodok.data.datasource.notification.NotificationRemoteDataSource
import com.team.todoktodok.data.local.nofitication.toDomain
import com.team.todoktodok.data.local.nofitication.toEntity

class DefaultNotificationRepository(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
    private val notificationLocalDataSource: NotificationLocalDataSource,
) : NotificationRepository {
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

    override suspend fun deletePushNotification() {
        notificationLocalDataSource.deletePushNotification()
    }

    override suspend fun getNotifications(): List<Notification> = notificationLocalDataSource.getNotifications().map { it.toDomain() }

    override suspend fun saveNotification(notification: Notification): Long =
        notificationLocalDataSource.saveNotifications(notification.toEntity())

    override suspend fun getNotificationCount(): Int = notificationLocalDataSource.getNotificationCount()

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
