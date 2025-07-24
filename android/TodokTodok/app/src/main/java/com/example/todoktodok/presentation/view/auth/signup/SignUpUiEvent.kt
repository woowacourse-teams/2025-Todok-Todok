package com.example.todoktodok.presentation.view.auth.signup

import com.example.domain.model.member.NickNameException

sealed interface SignUpUiEvent {
    data class ShowInvalidNickNameMessage(
        val exception: NickNameException,
    ) : SignUpUiEvent

    data object NavigateToMain : SignUpUiEvent
}
