package com.team.todoktodok.data.network.response.latest

import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val memberId: Long,
    val nickname: String,
    val profileImage: String,
) {
    fun toDomain(): User = User(memberId, Nickname(nickname), profileImage)
}
