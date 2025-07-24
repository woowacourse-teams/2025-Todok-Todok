package com.example.todoktodok.presentation.view.auth.login

sealed interface LoginUiEvent {
    object NavigateToMain : LoginUiEvent

    object NavigateToSignUp : LoginUiEvent
}
