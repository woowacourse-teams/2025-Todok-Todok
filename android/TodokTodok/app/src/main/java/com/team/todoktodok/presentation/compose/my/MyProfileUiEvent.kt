package com.team.todoktodok.presentation.compose.my

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface MyProfileUiEvent {
    data class ShowErrorMessage(
        val exceptions: TodokTodokExceptions,
    ) : MyProfileUiEvent
}
