package com.team.ui_compose.my

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface MyProfileUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : MyProfileUiEvent
}
