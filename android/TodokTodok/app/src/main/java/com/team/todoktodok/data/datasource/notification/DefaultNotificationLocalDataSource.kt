package com.team.todoktodok.data.datasource.notification

import com.team.todoktodok.data.local.nofitication.NotificationDao
import com.team.todoktodok.data.local.nofitication.NotificationDataStore
import com.team.todoktodok.data.local.nofitication.NotificationEntity

class DefaultNotificationLocalDataSource(
    private val dataStore: NotificationDataStore,
    private val dao: NotificationDao,
) : NotificationLocalDataSource {
    override suspend fun getFcmToken(): String? = dataStore.getFcmToken()

    override suspend fun getFId(): String? = dataStore.getFId()

    override suspend fun saveFcmToken(token: String) = dataStore.saveFcmToken(token)

    override suspend fun saveFId(id: String) = dataStore.saveFId(id)

    override suspend fun deletePushNotification() = dataStore.deletePushNotification()

    override suspend fun getNotifications(): List<NotificationEntity> = dao.getAllNotifications()

    override suspend fun saveNotifications(notificationEntity: NotificationEntity): Long = dao.saveNotification(notificationEntity)

    override suspend fun getNotificationCount(): Int = dao.getAllNotificationCount()
}
