package com.team.todoktodok.data.network.response.latest

import com.team.domain.model.latest.LatestDiscussion
import com.team.todoktodok.data.core.ext.toLocalDateTime
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.toDomain
import kotlinx.serialization.Serializable

@Serializable
data class LatestDiscussionResponse(
    val author: AuthorResponse,
    val book: BookResponse,
    val commentCount: Int,
    val content: String,
    val createdAt: String,
    val discussionId: Int,
    val isLikedByMe: Boolean,
    val likeCount: Int,
    val title: String,
) {
    fun toDomain(): LatestDiscussion =
        LatestDiscussion(
            author.toDomain(),
            book.toDomain(),
            commentCount,
            content,
            createdAt.toLocalDateTime(),
            discussionId,
            isLikedByMe,
            likeCount,
            title,
        )
}
