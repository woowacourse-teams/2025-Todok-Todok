package com.team.todoktodok.presentation.compose.discussion.hot

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface HotDiscussionUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : HotDiscussionUiEvent
}
