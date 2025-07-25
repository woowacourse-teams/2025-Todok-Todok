package com.example.todoktodok.data.network.request

import com.example.domain.model.Member
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
