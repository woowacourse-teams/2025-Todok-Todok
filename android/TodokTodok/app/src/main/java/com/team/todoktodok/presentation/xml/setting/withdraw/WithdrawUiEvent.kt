package com.team.todoktodok.presentation.xml.setting.withdraw

sealed interface WithdrawUiEvent {
    data object NavigateToLogin : WithdrawUiEvent
}
