package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion

data class DiscussionUiState(
    val item: Discussion,
    val opinionVisibility: Boolean = false,
) {
    val bookImage: String get() = item.book.image
    val bookTitle: String get() = item.book.extractSubtitle()
    val bookAuthor: String get() = item.book.extractAuthor()
    val discussionTitle: String get() = item.discussionTitle
    val discussionOpinion: String get() = item.discussionOpinion
    val writerNickname: String get() = item.writer.nickname.value
    val likeCount: String get() = item.likeCount.toString()
    val commentCount: String get() = item.commentCount.toString()
}
