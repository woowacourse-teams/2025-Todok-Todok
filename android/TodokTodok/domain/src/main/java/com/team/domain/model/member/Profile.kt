package com.team.domain.model.member

data class Profile(
    val memberId: Long,
    val nickname: String,
    val description: String,
    val profileImage: String,
)
