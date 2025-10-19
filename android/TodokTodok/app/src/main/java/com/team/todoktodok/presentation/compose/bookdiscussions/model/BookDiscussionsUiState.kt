package com.team.todoktodok.presentation.compose.bookdiscussions.model

data class BookDiscussionsUiState(
    val bookDetailSectionUiState: BookDetailSectionUiState = BookDetailSectionUiState(),
    val bookDiscussionsSectionUiState: BookDiscussionsSectionUiState = BookDiscussionsSectionUiState(),
) {
    fun updateBookDiscussions(transform: (BookDiscussionsSectionUiState) -> BookDiscussionsSectionUiState) =
        copy(bookDiscussionsSectionUiState = transform(bookDiscussionsSectionUiState))

    val isLoadingBookDiscussions get() = bookDiscussionsSectionUiState.isPagingLoading
}
