package com.team.ui_xml.auth.login

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface LoginUiEvent {
    data object ShowLoginButton : LoginUiEvent

    data object NavigateToMain : LoginUiEvent

    data object NavigateToSignUp : LoginUiEvent

    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : LoginUiEvent
}
