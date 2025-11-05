package com.team.data.network.response.notification

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
    val comment = "COMMENT"
    val reply = "REPLY"
    val discussion = "DISCUSSION"
    val like = "LIKE"

    val type: NotificationType =
        when (type) {
            comment -> NotificationType.Comment
            reply -> NotificationType.Reply
            like -> NotificationType.Like
            else -> throw IllegalArgumentException("Unknown type: $type")
        }

    val target: NotificationTarget =
        when (target) {
            comment -> NotificationTarget.Comment
            discussion -> NotificationTarget.Discussion
            reply -> NotificationTarget.Reply
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
