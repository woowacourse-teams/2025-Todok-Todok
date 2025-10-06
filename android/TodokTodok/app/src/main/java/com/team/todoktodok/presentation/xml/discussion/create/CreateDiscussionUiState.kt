package com.team.todoktodok.presentation.xml.discussion.create

import com.team.domain.model.Book

data class CreateDiscussionUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val opinion: String = "",
    val book: Book? = null,
    val discussionRoomId: Long? = null,
) {
    val isCreate: Boolean get() = title.isNotBlank() && opinion.isNotBlank()

    fun validate(): ErrorCreateDiscussionType? =
        when {
            title.isBlank() && opinion.isBlank() -> ErrorCreateDiscussionType.TITLE_AND_CONTENT_NOT_FOUND
            title.isBlank() -> ErrorCreateDiscussionType.TITLE_NOT_FOUND
            opinion.isBlank() -> ErrorCreateDiscussionType.CONTENT_NOT_FOUND
            else -> null
        }
}
