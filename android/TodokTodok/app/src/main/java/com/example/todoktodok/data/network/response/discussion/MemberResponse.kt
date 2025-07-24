package com.example.todoktodok.data.network.response.discussion

import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    @SerialName("memberId")
    val memberId: Long,
    @SerialName("nickname")
    val nickname: String,
)

fun MemberResponse.toDomain() = User(id = memberId, nickname = Nickname(nickname))
