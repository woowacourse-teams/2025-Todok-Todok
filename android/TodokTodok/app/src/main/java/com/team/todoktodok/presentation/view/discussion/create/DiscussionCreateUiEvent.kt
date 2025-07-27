package com.team.todoktodok.presentation.view.discussion.create

import com.team.domain.model.Note

sealed interface DiscussionCreateUiEvent {
    data object NavigateUp : DiscussionCreateUiEvent

    data object ShowOwnedNotes : DiscussionCreateUiEvent

    data object CreateDiscussion : DiscussionCreateUiEvent

    data class SelectNote(
        val note: Note,
    ) : DiscussionCreateUiEvent
}
