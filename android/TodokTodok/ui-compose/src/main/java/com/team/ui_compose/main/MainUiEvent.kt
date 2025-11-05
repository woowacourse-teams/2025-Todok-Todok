package com.team.ui_compose.main

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface MainUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : MainUiEvent

    data object ScrollToAllDiscussion : MainUiEvent
}
