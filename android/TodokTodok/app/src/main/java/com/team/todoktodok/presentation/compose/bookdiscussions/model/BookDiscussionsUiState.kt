package com.team.todoktodok.presentation.compose.bookdiscussions.model

sealed interface BookDiscussionsUiState {
    object Empty : BookDiscussionsUiState

    data class Success(
        val bookDetailSectionUiState: BookDetailSectionUiState,
        val bookDiscussionsSectionUiState: BookDiscussionsSectionUiState,
    ) : BookDiscussionsUiState {
        fun updateBookDiscussions(transform: (BookDiscussionsSectionUiState) -> BookDiscussionsSectionUiState): Success =
            copy(bookDiscussionsSectionUiState = transform(bookDiscussionsSectionUiState))

        val isLoadingBookDiscussions get() = bookDiscussionsSectionUiState.isPagingLoading
    }
}
