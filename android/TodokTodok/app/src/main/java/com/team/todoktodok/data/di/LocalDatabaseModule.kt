package com.team.todoktodok.data.di

import android.content.Context
import androidx.room.Room
import com.team.todoktodok.data.local.nofitication.DefaultNotificationDataStore
import com.team.todoktodok.data.local.nofitication.NotificationDao
import com.team.todoktodok.data.local.nofitication.NotificationDataStore
import com.team.todoktodok.data.local.nofitication.NotificationDatabase

class LocalDatabaseModule(
    context: Context,
) {
    val notificationDataStore: NotificationDataStore by lazy {
        DefaultNotificationDataStore(context)
    }

    val notificationDatabase: NotificationDao by lazy {
        Room
            .databaseBuilder(
                context,
                NotificationDatabase::class.java,
                DATABASE_NAME,
            ).build()
            .notificationDao()
    }

    companion object {
        private const val DATABASE_NAME: String = "notification"
    }
}
