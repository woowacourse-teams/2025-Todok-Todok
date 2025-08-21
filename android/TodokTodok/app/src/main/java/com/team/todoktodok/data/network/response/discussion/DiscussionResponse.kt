package com.team.todoktodok.data.network.response.discussion

import com.team.domain.model.Discussion
import com.team.todoktodok.data.core.ext.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionResponse(
    val book: BookResponse,
    val member: MemberResponse,
    val createdAt: String,
    val discussionId: Long,
    val discussionOpinion: String,
    val discussionTitle: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLikedByMe: Boolean,
)

fun DiscussionResponse.toDomain() =
    Discussion(
        id = discussionId,
        discussionTitle = discussionTitle,
        book = book.toDomain(),
        writer = member.toDomain(),
        createAt = createdAt.toLocalDateTime(),
        discussionOpinion = discussionOpinion,
        likeCount = likeCount,
        commentCount = commentCount,
        isLikedByMe = isLikedByMe,
    )
