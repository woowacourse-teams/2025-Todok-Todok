package com.team.todoktodok.data.datasource.notification

import com.team.todoktodok.data.local.nofitication.NotificationDatabase

class DefaultNotificationLocalDataSource(
    private val database: NotificationDatabase,
) : NotificationLocalDataSource {
    override suspend fun getFcmToken(): String? = database.getFcmToken()

    override suspend fun getFId(): String? = database.getFId()

    override suspend fun saveFcmToken(token: String) = database.saveFcmToken(token)

    override suspend fun saveFId(id: String) = database.saveFId(id)

    override suspend fun deletePushNotification() = database.deletePushNotification()
}
