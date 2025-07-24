package com.example.todoktodok.data.network.response.comment

import com.example.domain.model.Comment
import com.example.todoktodok.data.core.ext.toLocalDateTime
import com.example.todoktodok.data.network.response.discussion.MemberResponse
import com.example.todoktodok.data.network.response.discussion.toDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    @SerialName("commentId")
    val commentId: Long,
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("member")
    val member: MemberResponse,
)

fun CommentResponse.toDomain() = Comment(commentId, content, member.toDomain(), createdAt.toLocalDateTime())
