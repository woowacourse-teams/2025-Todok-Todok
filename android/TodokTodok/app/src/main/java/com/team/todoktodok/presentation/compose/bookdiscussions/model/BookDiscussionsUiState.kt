package com.team.todoktodok.presentation.compose.bookdiscussions.model

import androidx.compose.runtime.Immutable
import com.google.common.collect.ImmutableList
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.exception.TodokTodokExceptions

sealed interface BookDiscussionsUiState {
    object Loading : BookDiscussionsUiState

    data class Success(
        val book: BookDetailUiState,
        val discussions: ImmutableList<DiscussionItem>,
    ) : BookDiscussionsUiState

    data class Failure(
        val exception: TodokTodokExceptions,
    ) : BookDiscussionsUiState
}
