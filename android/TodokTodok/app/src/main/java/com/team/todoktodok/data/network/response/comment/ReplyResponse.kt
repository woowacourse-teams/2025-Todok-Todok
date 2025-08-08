package com.team.todoktodok.data.network.response.comment

import com.team.domain.model.Reply
import com.team.todoktodok.data.core.ext.toLocalDateTime
import com.team.todoktodok.data.network.response.discussion.MemberResponse
import com.team.todoktodok.data.network.response.discussion.toDomain
import kotlinx.serialization.Serializable

@Serializable
data class ReplyResponse(
    val content: String,
    val createdAt: String,
    val isLikedByMe: Boolean,
    val likeCount: Int,
    val member: MemberResponse,
    val replyId: Int,
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
