package com.team.ui_compose.bookdiscussions.model

import com.team.ui_compose.bookdiscussions.components.BookDiscussionsSectionUiState

data class BookDiscussionsUiState(
    val bookDetailSectionUiState: BookDetailSectionUiState = BookDetailSectionUiState(),
    val bookDiscussionsSectionUiState: BookDiscussionsSectionUiState = BookDiscussionsSectionUiState(),
) {
    fun updateBookDiscussions(transform: (BookDiscussionsSectionUiState) -> BookDiscussionsSectionUiState) =
        copy(bookDiscussionsSectionUiState = transform(bookDiscussionsSectionUiState))

    val isLoadingBookDiscussions get() = bookDiscussionsSectionUiState.isPagingLoading
}
