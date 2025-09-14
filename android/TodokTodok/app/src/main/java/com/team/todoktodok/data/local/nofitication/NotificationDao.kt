package com.team.todoktodok.data.local.nofitication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNotification(notificationEntity: NotificationEntity): Long

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getAllNotificationCount(): Int
}
