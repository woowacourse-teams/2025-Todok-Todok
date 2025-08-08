package com.team.todoktodok.data.network.response

import com.team.domain.model.member.BlockedMember
import com.team.todoktodok.data.core.ext.toLocalDate
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
            createdAt.toLocalDate(),
        )
}
