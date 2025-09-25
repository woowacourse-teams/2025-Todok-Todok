package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.notification.NotificationContent
import kotlinx.parcelize.Parcelize

@Parcelize
class SerializationNotificationContent(
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val nickname: SerializationNickname,
    val discussionTitle: String,
    val content: String,
    val type: SerializationNotificationType,
    val target: SerializationNotificationTarget,
    val createAt: Long = System.currentTimeMillis(),
) : Parcelable {
    fun toDomain(): NotificationContent =
        NotificationContent(
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

fun NotificationContent.toSerialization(): SerializationNotificationContent =
    SerializationNotificationContent(
        discussionId = discussionId,
        commentId = commentId,
        replyId = replyId,
        nickname = nickname.toSerialization(),
        discussionTitle = discussionTitle,
        content = content,
        type = type.toSerialization(),
        target = target.toSerialization(),
    )
