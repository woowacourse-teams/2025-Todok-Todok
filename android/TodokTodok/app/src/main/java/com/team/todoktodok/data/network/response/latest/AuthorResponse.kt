package com.team.todoktodok.data.network.response.latest

import com.team.domain.model.latest.Author
import kotlinx.serialization.Serializable

@Serializable
data class AuthorResponse(
    val email: String,
    val id: Int,
    val nickname: String,
    val profileImage: String,
) {
    fun toDomain(): Author =
        Author(
            email = email,
            id = id,
            nickname = nickname,
            profileImage = profileImage,
        )
}
