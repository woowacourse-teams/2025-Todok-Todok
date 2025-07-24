package com.example.todoktodok.presentation.view.discussion.create

import com.example.domain.model.Note

sealed interface DiscussionCreateUiEvent {
    data object NavigateUp : DiscussionCreateUiEvent

    data object ShowOwnedNotes : DiscussionCreateUiEvent

    data object CreateDiscussion : DiscussionCreateUiEvent

    data class SelectNote(
        val note: Note,
    ) : DiscussionCreateUiEvent
}
