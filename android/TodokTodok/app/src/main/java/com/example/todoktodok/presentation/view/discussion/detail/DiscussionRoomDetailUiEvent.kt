package com.example.todoktodok.presentation.view.discussion.detail

sealed interface DiscussionRoomDetailUiEvent {
    data object NavigateUp : DiscussionRoomDetailUiEvent

    data class AddComment(
        val content: String,
    ) : DiscussionRoomDetailUiEvent
}
