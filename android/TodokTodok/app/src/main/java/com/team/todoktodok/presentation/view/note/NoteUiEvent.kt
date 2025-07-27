package com.team.todoktodok.presentation.view.note

import com.team.todoktodok.presentation.view.serialization.SerializationBook

sealed interface NoteUiEvent {
    data class ShowOwnBooks(
        val books: List<SerializationBook>,
    ) : NoteUiEvent

    data object NotHasSelectedBook : NoteUiEvent

    data object OnCompleteSaveNote : NoteUiEvent
}
