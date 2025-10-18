package com.team.todoktodok.presentation.view.setting

sealed interface SettingUiEvent {
    data object NavigateToLogin : SettingUiEvent
}
