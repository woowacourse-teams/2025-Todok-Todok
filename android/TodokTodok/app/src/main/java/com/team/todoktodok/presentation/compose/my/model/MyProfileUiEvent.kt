package com.team.todoktodok.presentation.compose.my.model

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface MyProfileUiEvent {
    data class ShowErrorMessage(
        val exceptions: TodokTodokExceptions,
    ) : MyProfileUiEvent
}
