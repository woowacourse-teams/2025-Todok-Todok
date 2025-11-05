package com.team.data.network.response.comment

import com.team.data.core.ext.toLocalDateTime
import com.team.data.network.response.discussion.MemberResponse
import com.team.data.network.response.discussion.toDomain
import com.team.domain.model.Reply
import kotlinx.serialization.Serializable

@Serializable
data class ReplyResponse(
    val content: String,
    val createdAt: String,
    val isLikedByMe: Boolean,
    val likeCount: Int,
    val member: MemberResponse,
    val replyId: Long,
)

fun ReplyResponse.toDomain() =
    Reply(
        content,
        createdAt.toLocalDateTime(),
        likeCount,
        member.toDomain(),
        replyId,
        isLikedByMe,
    )
