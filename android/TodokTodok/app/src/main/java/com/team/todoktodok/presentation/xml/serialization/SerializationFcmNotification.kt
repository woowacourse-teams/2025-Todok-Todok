package com.team.todoktodok.presentation.xml.serialization

import android.os.Parcelable
import com.team.domain.model.notification.FcmNotification
import com.team.todoktodok.presentation.view.serialization.SerializationNickname
import com.team.todoktodok.presentation.view.serialization.SerializationNotificationTarget
import com.team.todoktodok.presentation.view.serialization.SerializationNotificationType
import com.team.todoktodok.presentation.view.serialization.toSerialization
import kotlinx.parcelize.Parcelize

@Parcelize
class SerializationFcmNotification(
    val notificationId: Int?,
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val nickname: SerializationNickname,
    val discussionTitle: String,
    val content: String,
    val type: SerializationNotificationType,
    val target: SerializationNotificationTarget,
) : Parcelable {
    fun toDomain(): FcmNotification =
        FcmNotification(
            notificationId = notificationId,
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

fun FcmNotification.toSerialization(): SerializationFcmNotification =
    SerializationFcmNotification(
        notificationId = notificationId,
        discussionId = discussionId,
        commentId = commentId,
        replyId = replyId,
        nickname = nickname.toSerialization(),
        discussionTitle = discussionTitle,
        content = content,
        type = type.toSerialization(),
        target = target.toSerialization(),
    )
