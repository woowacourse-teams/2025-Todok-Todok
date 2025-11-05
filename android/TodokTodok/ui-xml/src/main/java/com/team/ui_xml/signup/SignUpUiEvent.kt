package com.team.ui_xml.auth.signup

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.NickNameException

sealed interface SignUpUiEvent {
    data class ShowInvalidNickNameMessage(
        val exception: NickNameException,
    ) : SignUpUiEvent

    data object NavigateToMain : SignUpUiEvent

    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : SignUpUiEvent
}
