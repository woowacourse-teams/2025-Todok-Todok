package com.team.todoktodok.presentation.view.book

import com.team.domain.model.Book

sealed class SelectBookUiEvent {
    data object ShowSavedDiscussionRoom : SelectBookUiEvent()

    data object HideKeyboard : SelectBookUiEvent()

    data class NavigateToCreateDiscussionRoom(
        val book: Book,
    ) : SelectBookUiEvent()

    data class ShowErrorMessage(
        val message: SelectBookErrorType,
    ) : SelectBookUiEvent()
}
