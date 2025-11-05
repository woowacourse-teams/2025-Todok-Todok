package com.team.ui_compose.bookdiscussions.model

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface BookDiscussionsUiEvent {
    data class ShowError(
        val exceptions: TodokTodokExceptions,
    ) : BookDiscussionsUiEvent
}
