package com.team.todoktodok.adapter.model

import com.team.domain.model.member.Nickname
import com.team.domain.model.notification.NotificationTarget
import com.team.domain.model.notification.NotificationType

data class FcmNotification(
    val notificationId: Int?,
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val nickname: Nickname,
    val discussionTitle: String,
    val content: String,
    val type: NotificationType,
    val target: NotificationTarget,
) {
    companion object {
        fun FcmNotification(data: Map<String, String>): FcmNotification =
            FcmNotification(
                notificationId =
                    if (data["notificationId"] != "null") {
                        data["notificationId"]?.toInt()
                    } else {
                        null
                    },
                discussionId = data["discussionId"]?.toLong() ?: 0L,
                commentId =
                    if (data["commentId"] != "null") {
                        data["commentId"]?.toLong()
                    } else {
                        null
                    },
                replyId =
                    if (data["replyId"] != "null") {
                        data["replyId"]?.toLong()
                    } else {
                        null
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
