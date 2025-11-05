package com.team.data.network.response.comment

import com.team.data.core.ext.toLocalDateTime
import com.team.data.network.response.discussion.MemberResponse
import com.team.data.network.response.discussion.toDomain
import com.team.domain.model.Comment
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val commentId: Long,
    val content: String,
    val createdAt: String,
    val member: MemberResponse,
    val likeCount: Int,
    val replyCount: Int,
    val isLikedByMe: Boolean,
)

fun CommentResponse.toDomain() =
    Comment(
        commentId,
        content,
        member.toDomain(),
        createdAt.toLocalDateTime(),
        likeCount,
        replyCount,
        isLikedByMe,
    )
