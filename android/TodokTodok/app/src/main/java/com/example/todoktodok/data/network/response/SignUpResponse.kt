package com.example.todoktodok.data.network.response

import com.example.domain.model.Member
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val nickName: String,
    val profileImage: String,
    val email: String,
) {
    fun toDomain(): Member =
        Member(
            nickName = nickName,
            profileImage = profileImage,
            email = email,
        )
}
