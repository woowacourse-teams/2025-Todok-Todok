package com.team.todoktodok.presentation.xml.setting.manage

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface ManageBlockedMembersUiEvent {
    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : ManageBlockedMembersUiEvent
}
