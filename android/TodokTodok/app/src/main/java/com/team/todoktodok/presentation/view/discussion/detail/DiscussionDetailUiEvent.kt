package com.team.todoktodok.presentation.view.discussion.detail

sealed interface DiscussionDetailUiEvent {
    data object NavigateUp : DiscussionDetailUiEvent

    data class AddComment(
        val content: String,
    ) : DiscussionDetailUiEvent
}
