package com.team.todoktodok.presentation.compose.bookdiscussions.model

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface BookDiscussionsUiState {
    object Loading : BookDiscussionsUiState

    data class Success(
        val book: BookDetailSectionUiState,
        val bookDiscussionsSectionUiState: BookDiscussionsSectionUiState,
    ) : BookDiscussionsUiState

    data class Failure(
        val exception: TodokTodokExceptions,
    ) : BookDiscussionsUiState
}
