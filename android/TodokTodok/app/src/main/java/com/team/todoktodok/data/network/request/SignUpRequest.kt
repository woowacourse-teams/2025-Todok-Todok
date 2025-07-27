package com.team.todoktodok.data.network.request

import com.team.domain.model.Member
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val nickname: String,
    val profileImage: String,
    val email: String,
)

fun Member.toRequest() =
    SignUpRequest(
        nickname = nickName,
        profileImage = profileImage,
        email = email,
    )
