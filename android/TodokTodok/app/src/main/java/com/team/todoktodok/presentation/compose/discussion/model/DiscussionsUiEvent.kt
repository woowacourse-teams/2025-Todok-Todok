package com.team.todoktodok.presentation.compose.discussion.model

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface DiscussionsUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : DiscussionsUiEvent

    data object ScrollToAllDiscussion : DiscussionsUiEvent
}
