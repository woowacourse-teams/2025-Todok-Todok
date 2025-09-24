package com.team.todoktodok.presentation.xml.discussions

import com.team.domain.model.Discussion

data class DiscussionUiState(
    private val item: Discussion,
    val searchKeyword: String = DEFAULT_SEARCH_KEYWORD,
) {
    val bookImage: String get() = item.bookImage
    val bookTitle: String get() = item.getBookTitle()
    val bookAuthor: String get() = item.getBookAuthor()
    val discussionId: Long get() = item.id
    val discussionTitle: String get() = item.discussionTitle
    val discussionOpinion: String get() = item.discussionOpinion
    val writerNickname: String get() = item.writerNickname
    val writerProfileImage: String get() = item.writer.profileImage

    val likeCount: String get() = item.likeCount.toString()
    val commentCount: String get() = item.commentCount.toString()
    val viewCount: String get() = item.viewCount.toString()

    val isLikedByMe: Boolean get() = item.isLikedByMe

    companion object {
        const val DEFAULT_SEARCH_KEYWORD = ""
    }
}
