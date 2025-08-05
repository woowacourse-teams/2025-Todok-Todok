package com.team.todoktodok.presentation.view.profile

sealed interface ProfileUiEvent {
    data object OnCompleteSupport : ProfileUiEvent
}
