package com.team.todoktodok.presentation.utview.discussiondetail

sealed interface DiscussionDetailUiEvent {
    data object NavigateUp : DiscussionDetailUiEvent

    data class AddComment(
        val content: String,
    ) : DiscussionDetailUiEvent
}
