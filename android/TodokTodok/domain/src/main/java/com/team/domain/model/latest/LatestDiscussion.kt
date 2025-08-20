package com.team.domain.model.latest

import com.team.domain.model.Book
import java.time.LocalDateTime

data class LatestDiscussion(
    val authorResponse: Author,
    val book: Book,
    val commentCount: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val discussionId: Int,
    val isLikedByMe: Boolean,
    val likeCount: Int,
    val title: String,
)
