package com.team.todoktodok.presentation.compose.discussion.latest

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface LatestDiscussionsUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : LatestDiscussionsUiEvent
}
