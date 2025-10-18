package com.team.todoktodok.presentation.compose.main

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface MainUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : MainUiEvent

    data object ScrollToAllDiscussion : MainUiEvent
}
