package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.member.User
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val memberId: Long,
    val nickname: String,
    val profileImage: String,
)

fun MemberResponse.toDomain() = User(id = memberId, nickname = nickname, profileImage = profileImage)
