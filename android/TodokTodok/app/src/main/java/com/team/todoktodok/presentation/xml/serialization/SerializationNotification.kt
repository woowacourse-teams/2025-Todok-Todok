package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.notification.Notification
import kotlinx.parcelize.Parcelize

@Parcelize
class SerializationNotification(
    val id: Long,
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val nickname: SerializationNickname,
    val discussionTitle: String,
    val content: String,
    val type: SerializationNotificationType,
    val target: SerializationNotificationTarget,
    val receivedAt: Long = System.currentTimeMillis(),
) : Parcelable {
    fun toDomain(): Notification =
        Notification(
            id = id,
            discussionId = discussionId,
            commentId = commentId,
            replyId = replyId,
            nickname = nickname.toDomain(),
            discussionTitle = discussionTitle,
            content = content,
            type = type.toDomain(),
            target = target.toDomain(),
        )
}

fun Notification.toSerialization(): SerializationNotification =
    SerializationNotification(
        id = id,
        discussionId = discussionId,
        commentId = commentId,
        replyId = replyId,
        nickname = nickname.toSerialization(),
        discussionTitle = discussionTitle,
        content = content,
        type = type.toSerialization(),
        target = target.toSerialization(),
    )
