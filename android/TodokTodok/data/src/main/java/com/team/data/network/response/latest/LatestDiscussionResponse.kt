package com.team.data.network.response.latest

import com.team.data.core.ext.toLocalDateTime
import com.team.data.network.response.discussion.BookResponse
import com.team.data.network.response.discussion.toDomain
import com.team.domain.model.Discussion
import kotlinx.serialization.Serializable

@Serializable
data class LatestDiscussionResponse(
    val member: MemberResponse,
    val book: BookResponse,
    val commentCount: Long,
    val discussionOpinion: String,
    val discussionTitle: String,
    val createdAt: String,
    val discussionId: Long,
    val isLikedByMe: Boolean,
    val viewCount: Long,
    val likeCount: Long,
) {
    fun toDomain(): Discussion =
        Discussion(
            id = discussionId,
            writer = member.toDomain(),
            book = book.toDomain(),
            commentCount = commentCount,
            discussionOpinion = discussionOpinion,
            createAt = createdAt.toLocalDateTime(),
            isLikedByMe = isLikedByMe,
            viewCount = viewCount,
            likeCount = likeCount,
            discussionTitle = discussionTitle,
        )
}
