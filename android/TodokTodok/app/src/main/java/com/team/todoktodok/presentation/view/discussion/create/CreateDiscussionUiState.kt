package com.team.todoktodok.presentation.view.discussion.create

import com.team.domain.model.Book

data class CreateDiscussionUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val opinion: String = "",
    val book: Book? = null,
    val discussionRoomId: Long? = null,
) {
    val isCreate: Boolean get() = title.isNotBlank() && opinion.isNotBlank()
}
