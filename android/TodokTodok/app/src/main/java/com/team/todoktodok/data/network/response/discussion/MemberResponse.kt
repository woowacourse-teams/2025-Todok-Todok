package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String,
)

fun MemberResponse.toDomain() = User(id = memberId, nickname = Nickname(nickname), profileImage = profileImage)
