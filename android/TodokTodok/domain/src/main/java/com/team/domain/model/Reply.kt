package com.team.domain.model

import com.team.domain.model.member.User
import java.time.LocalDateTime

data class Reply(
    val content: String,
    val createdAt: LocalDateTime,
    val likeCount: Int,
    val writer: User,
    val replyId: Long,
    val isLikedByMe: Boolean,
)
