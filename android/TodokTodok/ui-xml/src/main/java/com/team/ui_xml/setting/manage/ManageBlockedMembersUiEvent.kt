package com.team.ui_xml.setting.manage

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface ManageBlockedMembersUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : ManageBlockedMembersUiEvent
}
