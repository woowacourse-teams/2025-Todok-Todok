package com.team.todoktodok.presentation.view.setting.modify

import com.team.domain.model.member.NickNameException

sealed interface ModifyProfileUiEvent {
    data class ShowInvalidNickNameMessage(
        val exception: NickNameException,
    ) : ModifyProfileUiEvent

    data object OnCompleteModification : ModifyProfileUiEvent
}
