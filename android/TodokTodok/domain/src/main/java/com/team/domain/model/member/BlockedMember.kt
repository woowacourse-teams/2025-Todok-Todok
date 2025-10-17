package com.team.domain.model.member

import java.time.LocalDateTime

data class BlockedMember(
    val memberId: Long,
    val nickname: String,
    val createdAt: LocalDateTime,
)
