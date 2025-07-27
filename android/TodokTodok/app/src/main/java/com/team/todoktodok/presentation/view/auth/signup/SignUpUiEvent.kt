package com.team.todoktodok.presentation.view.auth.signup

import com.team.domain.model.member.NickNameException

sealed interface SignUpUiEvent {
    data class ShowInvalidNickNameMessage(
        val exception: NickNameException,
    ) : SignUpUiEvent

    data object NavigateToMain : SignUpUiEvent
}
