package com.team.todoktodok.data.datasource.notification

import android.content.Context
import com.team.todoktodok.data.local.notification.DefaultNotificationDataStore
import javax.inject.Inject

class DefaultNotificationLocalDataSource
    @Inject
    constructor(
        context: Context,
    ) : NotificationLocalDataSource {
        private val dataStore = DefaultNotificationDataStore(context)

        override suspend fun getFcmToken(): String? = dataStore.getFcmToken()

        override suspend fun getFId(): String? = dataStore.getFId()

        override suspend fun saveFcmToken(token: String) = dataStore.saveFcmToken(token)

        override suspend fun saveFId(id: String) = dataStore.saveFId(id)

        override suspend fun allowedNotification(isAllowed: Boolean) {
            dataStore.allowedNotification(isAllowed)
        }

        override suspend fun getIsNotificationAllowed(): Boolean? = dataStore.getIsNotificationAllowed()
    }
