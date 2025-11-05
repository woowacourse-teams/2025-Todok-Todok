package com.team.ui_xml.setting.withdraw

sealed interface WithdrawUiEvent {
    data object NavigateToLogin : WithdrawUiEvent
}
