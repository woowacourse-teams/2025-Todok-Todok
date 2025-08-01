package com.team.todoktodok.presentation.view.discussiondetail

sealed interface DiscussionDetailUiEvent {
    data object NavigateUp : DiscussionDetailUiEvent

    data object ShowCreateComment : DiscussionDetailUiEvent
}
