package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion

data class DiscussionUiState(
    val item: Discussion,
    val opinionVisibility: Boolean = false,
) {
    val bookImage: String get() = item.bookImage
    val bookTitle: String get() = item.getBookTitle()
    val bookAuthor: String get() = item.getBookAuthor()
    val discussionTitle: String get() = item.discussionTitle
    val discussionOpinion: String get() = item.discussionOpinion
    val writerNickname: String get() = item.writerNickname
    val likeCount: String get() = item.likeCount.toString()
    val commentCount: String get() = item.commentCount.toString()
    val viewCount: String get() = item.viewCount.toString()
}
