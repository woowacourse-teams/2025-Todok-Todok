package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.notification.NotificationType
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SerializationNotificationType : Parcelable {
    LIKE,
    COMMENT,
    REPLY,
    ;

    fun toDomain(): NotificationType =
        when (this) {
            LIKE -> NotificationType.Like
            COMMENT -> NotificationType.Comment
            REPLY -> NotificationType.Reply
        }
}

fun NotificationType.toSerialization(): SerializationNotificationType =
    when (this) {
        is NotificationType.Like -> SerializationNotificationType.LIKE
        is NotificationType.Comment -> SerializationNotificationType.COMMENT
        is NotificationType.Reply -> SerializationNotificationType.REPLY
    }
