package com.team.todoktodok.data.di

import android.content.Context
import com.team.todoktodok.data.local.nofitication.DefaultNotificationDatabase
import com.team.todoktodok.data.local.nofitication.NotificationDatabase

class LocalDatabaseModule(
    context: Context,
) {
    val notificationDatabase: NotificationDatabase by lazy {
        DefaultNotificationDatabase(context)
    }
}
