package com.team.todoktodok.presentation.view.profile

import com.team.domain.model.Support
import com.team.domain.model.exception.TokdokTodokExceptions

sealed interface ProfileUiEvent {
    data class OnCompleteSupport(
        val type: Support,
    ) : ProfileUiEvent

    data class ShowErrorMessage(
        val exceptions: TokdokTodokExceptions,
    ) : ProfileUiEvent
}
