package com.team.domain.model.notification

import com.team.domain.model.member.Nickname

data class NotificationContent(
    val discussionId: Long,
    val commentId: Long?,
    val replyId: Long?,
    val nickname: Nickname,
    val discussionTitle: String,
    val content: String,
    val type: NotificationType,
    val target: NotificationTarget,
)