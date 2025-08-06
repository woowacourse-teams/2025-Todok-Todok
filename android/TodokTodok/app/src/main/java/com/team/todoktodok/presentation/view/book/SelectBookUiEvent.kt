package com.team.todoktodok.presentation.view.book

import com.team.domain.model.Book
import com.team.domain.model.Books

sealed class SelectBookUiEvent {
    data object StartLoading : SelectBookUiEvent()

    data object FinishLoading : SelectBookUiEvent()

    data object HideKeyboard : SelectBookUiEvent()

    data object RevealKeyboard : SelectBookUiEvent()

    data class NavigateToCreateDiscussionRoom(
        val book: Book,
    ) : SelectBookUiEvent()

    data class ShowToast(
        val error: ErrorSelectBookType,
    ) : SelectBookUiEvent()

    data class ShowSearchResult(
        val books: Books,
    ) : SelectBookUiEvent()
}
