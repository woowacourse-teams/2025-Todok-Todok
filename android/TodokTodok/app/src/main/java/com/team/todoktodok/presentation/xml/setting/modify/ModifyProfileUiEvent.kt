package com.team.todoktodok.presentation.xml.setting.modify

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.NickNameException

sealed interface ModifyProfileUiEvent {
    data class ShowInvalidNickNameMessage(
        val exception: NickNameException,
    ) : ModifyProfileUiEvent

    data object OnCompleteModification : ModifyProfileUiEvent

    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : ModifyProfileUiEvent
}
