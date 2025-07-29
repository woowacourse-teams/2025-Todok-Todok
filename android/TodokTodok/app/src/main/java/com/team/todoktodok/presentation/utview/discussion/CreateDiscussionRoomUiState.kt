package com.team.todoktodok.presentation.utview.discussion

import com.team.domain.model.Book

data class CreateDiscussionRoomUiState(
    val isLoading: Boolean = false,
    val searchInput: String? = null,
    val selectedBook: Book? = null,
    val searchedBooks: List<Book> = emptyList(),
    val discussionTitle: String? = null,
    val discussionContent: String? = null,
    val errorMessage: String? = null,
)
