package com.team.todoktodok.presentation.compose.discussion.model

import com.team.domain.model.Discussion

data class DiscussionUiModel(
    private val item: Discussion,
) {
    val bookImage: String get() = item.bookImage
    val bookTitle: String get() = item.getBookTitle()
    val bookAuthor: String get() = item.getBookAuthor()
    val discussionId: Long get() = item.id
    val discussionTitle: String get() = item.discussionTitle
    val discussionOpinion: String get() = item.discussionOpinion
    val writerNickname: String get() = item.writerNickname
    val writerProfileImage: String get() = item.writer.profileImage
    val writerId: Long get() = item.writer.id

    val likeCount: String get() = item.likeCount.toString()
    val commentCount: String get() = item.commentCount.toString()
    val viewCount: String get() = item.viewCount.toString()

    val isLikedByMe: Boolean get() = item.isLikedByMe
}
