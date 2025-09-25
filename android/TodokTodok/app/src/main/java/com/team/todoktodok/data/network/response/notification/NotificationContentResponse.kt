package com.team.todoktodok.data.network.response.notification

import com.team.domain.model.member.Nickname
import com.team.domain.model.notification.NotificationContent
import com.team.domain.model.notification.NotificationTarget
import com.team.domain.model.notification.NotificationType
import kotlinx.serialization.Serializable

@Serializable
data class NotificationContentResponse(
    val notificationId: Long,
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val memberNickname: String,
    val discussionTitle: String,
    val content: String,
    val type: String,
    val target: String,
)

fun NotificationContentResponse.toDomain(): NotificationContent {
    val type: NotificationType =
        when (type) {
            "COMMENT" -> NotificationType.Comment
            "REPLY" -> NotificationType.Reply
            "LIKE" -> NotificationType.Like
            else -> throw IllegalArgumentException("Unknown type: $type")
        }

    val target: NotificationTarget =
        when (target) {
            "DISCUSSION" -> NotificationTarget.Discussion
            "COMMENT" -> NotificationTarget.Comment
            "REPLY" -> NotificationTarget.Reply
            else -> throw IllegalArgumentException("Unknown target: $target")
        }

    return NotificationContent(
        discussionId = discussionId,
        commentId = commentId,
        replyId = replyId,
        nickname = Nickname(memberNickname),
        discussionTitle = discussionTitle,
        content = content,
        type = type,
        target = target,
    )
}
