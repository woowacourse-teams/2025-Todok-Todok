package com.team.ui_xml.create

import com.team.domain.model.Book
import com.team.domain.model.book.SearchedBook

data class CreateDiscussionUiState(
    val isLoading: Boolean = false,
    val isPosting: Boolean = false,
    val title: String = "",
    val opinion: String = "",
    val book: SearchedBook? = null,
    val draftBook: SearchedBook? = null,
    val editBook: Book? = null,
    val discussionRoomId: Long? = null,
    val draftDiscussionCount: Int = 0,
) {
    val isCreate: Boolean get() = title.isNotBlank() && opinion.isNotBlank()

    val isDraft: Boolean get() = title.isNotBlank() || opinion.isNotBlank()

    fun validate(): ErrorCreateDiscussionType? =
        when {
            title.isBlank() && opinion.isBlank() -> ErrorCreateDiscussionType.TITLE_AND_CONTENT_NOT_FOUND
            title.isBlank() -> ErrorCreateDiscussionType.TITLE_NOT_FOUND
            opinion.isBlank() -> ErrorCreateDiscussionType.CONTENT_NOT_FOUND
            else -> null
        }
}
