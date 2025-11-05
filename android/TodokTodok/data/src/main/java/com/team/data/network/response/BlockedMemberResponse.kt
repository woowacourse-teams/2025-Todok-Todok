package com.team.data.network.response

import com.team.data.core.ext.toLocalDateTime
import com.team.domain.model.member.BlockedMember
import kotlinx.serialization.Serializable

@Serializable
data class BlockedMemberResponse(
    val memberId: Long,
    val nickname: String,
    val createdAt: String,
) {
    fun toDomain(): BlockedMember =
        BlockedMember(
            memberId,
            nickname,
            createdAt.toLocalDateTime(),
        )
}
