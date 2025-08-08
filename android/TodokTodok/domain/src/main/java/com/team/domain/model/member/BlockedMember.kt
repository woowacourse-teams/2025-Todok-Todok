package com.team.domain.model.member

import java.time.LocalDate

data class BlockedMember(
    val memberId: Long,
    val nickname: String,
    val createdAt: LocalDate,
)
