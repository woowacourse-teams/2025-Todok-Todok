package com.team.todoktodok.presentation.view.setting.withdraw

sealed interface WithdrawUiEvent {
    data object NavigateToLogin : WithdrawUiEvent
}
