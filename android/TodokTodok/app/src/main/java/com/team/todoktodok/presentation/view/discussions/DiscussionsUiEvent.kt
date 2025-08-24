package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface DiscussionsUiEvent {
    data object ShowNotHasAllDiscussions : DiscussionsUiEvent

    data object ShowHasAllDiscussions : DiscussionsUiEvent

    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : DiscussionsUiEvent
}
