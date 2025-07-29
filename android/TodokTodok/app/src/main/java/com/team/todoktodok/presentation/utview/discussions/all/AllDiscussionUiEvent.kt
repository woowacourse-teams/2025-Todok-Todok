package com.team.todoktodok.presentation.utview.discussions.all

sealed interface AllDiscussionUiEvent {
    data class NavigateToDetail(
        val id: Long,
    ) : AllDiscussionUiEvent
}
