package com.team.todoktodok.presentation.view.serialization

import com.team.domain.model.notification.NotificationTarget

enum class SerializationNotificationTarget {
    DISCUSSION,
    COMMENT,
    REPLY,
    ;

    fun toDomain(): NotificationTarget =
        when (this) {
            DISCUSSION -> NotificationTarget.Discussion
            COMMENT -> NotificationTarget.Comment
            REPLY -> NotificationTarget.Reply
        }
}

fun NotificationTarget.toSerialization(): SerializationNotificationTarget =
    when (this) {
        is NotificationTarget.Discussion -> SerializationNotificationTarget.DISCUSSION
        is NotificationTarget.Comment -> SerializationNotificationTarget.COMMENT
        is NotificationTarget.Reply -> SerializationNotificationTarget.REPLY
    }
