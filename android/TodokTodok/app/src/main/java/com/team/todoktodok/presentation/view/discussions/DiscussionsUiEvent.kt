package com.team.todoktodok.presentation.view.discussions

sealed interface DiscussionsUiEvent {
    data class NavigateToDetail(
        val id: Long,
    ) : DiscussionsUiEvent

    data object ShowNotHasMyDiscussions : DiscussionsUiEvent

    data object ShowNotHasAllDiscussions : DiscussionsUiEvent

    data object ShowHasMyDiscussions : DiscussionsUiEvent

    data object ShowHasAllDiscussions : DiscussionsUiEvent
}
