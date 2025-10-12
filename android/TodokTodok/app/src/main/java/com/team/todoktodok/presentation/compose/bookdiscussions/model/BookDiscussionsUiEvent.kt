package com.team.todoktodok.presentation.compose.bookdiscussions.model

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface BookDiscussionsUiEvent {
    data class ShowErrorEvent(
        val exceptions: TodokTodokExceptions,
    ) : BookDiscussionsUiEvent
}
