package com.team.todoktodok.presentation.compose.bookdiscussions.model

import androidx.compose.runtime.Immutable
import com.team.domain.model.Discussion

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
    val viewCount: Long,
    val commentCount: Long,
)

fun Discussion.toDiscussionItem() =
    DiscussionItem(
        discussionId = id,
        bookImage = book.image,
        bookTitle = book.title,
        bookAuthor = book.author,
        discussionTitle = discussionTitle,
        writerProfile = writer.profileImage,
        writerName = writer.nickname.value,
        isLikedByMe = isLikedByMe,
        likeCount = likeCount,
        viewCount = viewCount,
        commentCount = commentCount,
    )
