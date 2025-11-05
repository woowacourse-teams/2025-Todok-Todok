package com.team.ui_xml.setting.modify

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.ProfileException

sealed interface ModifyProfileUiEvent {
    data class ShowInvalidNickNameMessage(
        val exception: NickNameException,
    ) : ModifyProfileUiEvent

    data class ShowInvalidMessageMessage(
        val exception: ProfileException,
    ) : ModifyProfileUiEvent

    data object OnCompleteModification : ModifyProfileUiEvent

    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : ModifyProfileUiEvent
}
