package com.example.todoktodok.presentation.view.discussion.create

sealed interface DiscussionCreateUiEvent {
    data object ShowOwnedNotes : DiscussionCreateUiEvent

    data object CreateDiscussion : DiscussionCreateUiEvent
}
