package com.team.todoktodok.presentation.view.discussions

sealed interface DiscussionsUiEvent {
    data object ShowNotHasMyDiscussions : DiscussionsUiEvent

    data object ShowNotHasAllDiscussions : DiscussionsUiEvent

    data object ShowHasMyDiscussions : DiscussionsUiEvent

    data object ShowHasAllDiscussions : DiscussionsUiEvent
}
