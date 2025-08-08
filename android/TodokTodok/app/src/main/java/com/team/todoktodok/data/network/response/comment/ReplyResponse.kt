package com.team.todoktodok.data.network.response.comment

import com.team.todoktodok.data.network.response.discussion.MemberResponse
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
