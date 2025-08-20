package com.team.domain.model.latest

import com.team.domain.model.Book
import java.time.LocalDateTime

data class LatestDiscussion(
    val discussionId: Long,
    val author: Author,
    val book: Book,
    val commentCount: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val isLikedByMe: Boolean,
    val likeCount: Int,
    val title: String,
)
