package com.example.todoktodok.presentation.view.note

import com.example.todoktodok.presentation.view.parcelable.ParcelableBook

sealed interface NoteUiEvent {
    data class ShowOwnBooks(
        val books: List<ParcelableBook>,
    ) : NoteUiEvent
}
