package com.team.todoktodok.data.local.nofitication

import androidx.room.TypeConverter
import com.team.domain.model.notification.NotificationTarget
import com.team.domain.model.notification.NotificationType

class Converters {
    @TypeConverter
    fun fromNotificationType(type: NotificationType): String =
        when (type) {
            NotificationType.Like -> "LIKE"
            NotificationType.Comment -> "COMMENT"
            NotificationType.Reply -> "REPLY"
        }

    @TypeConverter
    fun toNotificationType(value: String): NotificationType =
        when (value) {
            "LIKE" -> NotificationType.Like
            "COMMENT" -> NotificationType.Comment
            "REPLY" -> NotificationType.Reply
            else -> throw IllegalArgumentException("Unknown NotificationType: $value")
        }

    @TypeConverter
    fun fromNotificationTarget(target: NotificationTarget): String =
        when (target) {
            NotificationTarget.Discussion -> "DISCUSSION"
            NotificationTarget.Comment -> "COMMENT"
            NotificationTarget.Reply -> "REPLY"
        }

    @TypeConverter
    fun toNotificationTarget(value: String): NotificationTarget =
        when (value) {
            "DISCUSSION" -> NotificationTarget.Discussion
            "COMMENT" -> NotificationTarget.Comment
            "REPLY" -> NotificationTarget.Reply
            else -> throw IllegalArgumentException("Unknown NotificationTarget: $value")
        }
}
