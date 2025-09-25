package com.team.todoktodok.data.datasource.notification

import com.team.todoktodok.data.local.notification.NotificationDataStore

class DefaultNotificationLocalDataSource(
    private val dataStore: NotificationDataStore,
) : NotificationLocalDataSource {
    override suspend fun getFcmToken(): String? = dataStore.getFcmToken()

    override suspend fun getFId(): String? = dataStore.getFId()

    override suspend fun saveFcmToken(token: String) = dataStore.saveFcmToken(token)

    override suspend fun saveFId(id: String) = dataStore.saveFId(id)
}
