package com.team.data.network.response.discussion

import com.team.data.core.ext.toLocalDateTime
import com.team.domain.model.Discussion
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionResponse(
    val book: BookResponse,
    val member: MemberResponse,
    val createdAt: String,
    val discussionId: Long,
    val discussionOpinion: String,
    val discussionTitle: String,
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long,
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
        viewCount = viewCount,
        likeCount = likeCount,
        commentCount = commentCount,
        isLikedByMe = isLikedByMe,
    )
