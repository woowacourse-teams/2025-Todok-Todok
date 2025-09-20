package com.team.domain.model.notification

import com.team.domain.model.member.Nickname
import java.time.LocalDateTime

data class Notification(
    val id: Long,
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val nickname: Nickname,
    val discussionTitle: String,
    val content: String,
    val type: NotificationType,
    val target: NotificationTarget,
    val receivedAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun Notification(data: Map<String, String>): Notification =
            Notification(
                id = data["id"]?.toLong() ?: 0L,
                discussionId = data["discussionId"]?.toLong() ?: 0L,
                commentId =
                    if (data["commentId"] == "null") {
                        null
                    } else {
                        data["commentId"]?.toLong()
                    },
                replyId =
                    if (data["replyId"] == "null") {
                        null
                    } else {
                        data["replyId"]?.toLong()
                    },
                nickname = Nickname(data["memberNickname"] ?: ""),
                discussionTitle = data["discussionTitle"] ?: "",
                content = data["content"] ?: "",
                type =
                    when (data["type"]) {
                        "LIKE" -> NotificationType.Like
                        "COMMENT" -> NotificationType.Comment
                        "REPLY" -> NotificationType.Reply
                        else -> throw IllegalArgumentException("Unknown type: ${data["type"]}")
                    },
                target =
                    when (data["target"]) {
                        "DISCUSSION" -> NotificationTarget.Discussion
                        "COMMENT" -> NotificationTarget.Comment
                        "REPLY" -> NotificationTarget.Reply
                        else -> throw IllegalArgumentException("Unknown target: ${data["target"]}")
                    },
            )
    }
}
