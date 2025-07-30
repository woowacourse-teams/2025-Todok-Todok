package com.team.todoktodok.presentation.utview.discussions

sealed interface DiscussionsUiEvent {
    data class NavigateToDetail(
        val id: Long,
    ) : DiscussionsUiEvent

    data object ShowNotHasMyDiscussions : DiscussionsUiEvent

    data object ShowNotHasAllDiscussions : DiscussionsUiEvent

    data object ShowHasMyDiscussions : DiscussionsUiEvent

    data object ShowHasAllDiscussions : DiscussionsUiEvent
}
