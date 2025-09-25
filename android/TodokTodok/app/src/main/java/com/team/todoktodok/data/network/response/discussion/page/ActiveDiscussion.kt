package com.team.todoktodok.data.network.response.discussion.page

import com.team.domain.model.Discussion
import com.team.todoktodok.data.core.ext.toLocalDateTime
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.MemberResponse
import com.team.todoktodok.data.network.response.discussion.toDomain
import kotlinx.serialization.Serializable

@Serializable
data class ActiveDiscussion(
    val discussionId: Long,
    val book: BookResponse,
    val member: MemberResponse,
    val createdAt: String,
    val discussionTitle: String,
    val discussionOpinion: String,
    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val isLikedByMe: Boolean,
    val lastCommentedAt: String,
) {
    fun toDomain() =
        Discussion(
            id = discussionId,
            discussionTitle = discussionTitle,
            discussionOpinion = discussionOpinion,
            book = book.toDomain(),
            writer = member.toDomain(),
            viewCount = viewCount,
            likeCount = likeCount,
            commentCount = commentCount,
            isLikedByMe = isLikedByMe,
            createAt = createdAt.toLocalDateTime(),
        )
}
