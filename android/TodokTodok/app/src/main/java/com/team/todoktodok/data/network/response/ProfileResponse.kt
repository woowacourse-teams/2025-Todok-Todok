package com.team.todoktodok.data.network.response

import com.team.domain.model.member.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val memberId: Long,
    val nickname: String,
    val profileMessage: String,
    val profileImage: String,
) {
    fun toDomain(): Profile =
        Profile(
            memberId,
            nickname,
            profileMessage,
            profileImage,
        )
}
