package com.team.domain.model

import com.team.domain.model.member.User
import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val content: String,
    val writer: User,
    val createAt: LocalDateTime,
)
