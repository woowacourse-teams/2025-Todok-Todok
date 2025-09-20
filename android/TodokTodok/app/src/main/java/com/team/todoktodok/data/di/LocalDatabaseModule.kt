package com.team.todoktodok.data.di

import android.content.Context
import com.team.todoktodok.data.local.nofitication.DefaultNotificationDataStore
import com.team.todoktodok.data.local.nofitication.NotificationDataStore

class LocalDatabaseModule(
    context: Context,
) {
    val notificationDataStore: NotificationDataStore by lazy {
        DefaultNotificationDataStore(context)
    }
}
