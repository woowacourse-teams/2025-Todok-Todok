package com.team.todoktodok.data.network.response.latest

import com.team.domain.model.latest.Author
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val memberId: Long,
    val nickname: String,
    val profileImage: String,
) {
    fun toDomain(): Author = Author(memberId, nickname, profileImage)
}
