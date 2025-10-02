package com.team.todoktodok.presentation.compose.discussion.all

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface AllDiscussionUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : AllDiscussionUiEvent
}
