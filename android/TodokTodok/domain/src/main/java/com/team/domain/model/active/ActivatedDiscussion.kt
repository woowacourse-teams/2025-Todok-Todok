package com.team.domain.model.active

import com.team.domain.model.Book
import com.team.domain.model.member.User
import java.time.LocalDateTime

data class ActivatedDiscussion(
    val discussionId: Long,
    val book: Book,
    val writer: User,
    val createdAt: LocalDateTime,
    val discussionTitle: String,
    val discussionOpinion: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLikedByMe: Boolean,
    val lastCommentedAt: String,
)
