package com.team.todoktodok.presentation.compose.bookdiscussions.model

import androidx.compose.runtime.Immutable

@Immutable
data class DiscussionItem(
    val discussionId: Long,
    val bookImage: String,
    val bookTitle: String,
    val bookAuthor: String,
    val discussionTitle: String,
    val writerProfile: String,
    val writerName: String,
    val isLikedByMe: Boolean,
    val likeCount: Long,
    val viewsCount: Long,
    val commentCount: Long,
)
