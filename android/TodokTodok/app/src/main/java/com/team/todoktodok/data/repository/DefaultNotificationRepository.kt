package com.team.todoktodok.data.repository

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.model.notification.Notification
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.data.datasource.notification.NotificationLocalDataSource
import com.team.todoktodok.data.datasource.notification.NotificationRemoteDataSource
import com.team.todoktodok.data.network.response.notification.toDomain

class DefaultNotificationRepository(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
    private val notificationLocalDataSource: NotificationLocalDataSource,
) : NotificationRepository {
    override suspend fun registerPushNotification(): NetworkResult<Unit> {
        val fcmToken = notificationLocalDataSource.getFcmToken()
        val fId = notificationLocalDataSource.getFId()
        if (fcmToken.isNullOrBlank() || fId.isNullOrBlank()) {
            return NetworkResult.Success(Unit)
        }
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

    override suspend fun getNotifications(): NetworkResult<Pair<Int, List<Notification>>> =
        notificationRemoteDataSource
            .getNotification()
            .map { it.unreadCount to it.notifications.map { it.toDomain() } }

    override suspend fun getUnreadNotificationsCount(): NetworkResult<Boolean> =
        notificationRemoteDataSource.getUnreadNotificationsCount().map { it.exist }

    override suspend fun deleteNotification(notificationId: Long): NetworkResult<Unit> =
        notificationRemoteDataSource.deleteNotification(notificationId)

    override suspend fun readNotification(notificationId: Long): NetworkResult<Unit> =
        notificationRemoteDataSource.readNotification(notificationId)

    override suspend fun allowedNotification(isReject: Boolean) {
        notificationLocalDataSource.allowedNotification(isReject)
    }

    override suspend fun getIsNotificationAllowed(): Boolean? =
        notificationLocalDataSource.getIsNotificationAllowed()


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
