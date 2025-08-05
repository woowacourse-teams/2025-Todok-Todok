package com.team.todoktodok.presentation.view.profile

import com.team.domain.model.Support

sealed interface ProfileUiEvent {
    data class OnCompleteSupport(
        val type: Support,
    ) : ProfileUiEvent
}
