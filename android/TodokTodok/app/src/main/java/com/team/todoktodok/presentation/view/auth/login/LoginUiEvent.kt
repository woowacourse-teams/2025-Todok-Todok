package com.team.todoktodok.presentation.view.auth.login

sealed interface LoginUiEvent {
    data object ShowLoginButton : LoginUiEvent

    data object NavigateToMain : LoginUiEvent

    data object NavigateToSignUp : LoginUiEvent
}
