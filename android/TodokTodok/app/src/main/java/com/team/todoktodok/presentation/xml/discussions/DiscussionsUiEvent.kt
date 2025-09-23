package com.team.todoktodok.presentation.xml.discussions

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface DiscussionsUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : DiscussionsUiEvent

    data object ShowSearchResult : DiscussionsUiEvent
}
