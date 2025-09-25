package com.team.todoktodok.data.di

import android.content.Context
import com.team.todoktodok.data.local.notification.DefaultNotificationDataStore
import com.team.todoktodok.data.local.notification.NotificationDataStore

class LocalDatabaseModule(
    context: Context,
) {
    val notificationDataStore: NotificationDataStore by lazy {
        DefaultNotificationDataStore(context)
    }
}
