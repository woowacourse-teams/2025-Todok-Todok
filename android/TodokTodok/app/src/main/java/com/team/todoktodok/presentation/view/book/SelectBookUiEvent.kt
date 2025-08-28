package com.team.todoktodok.presentation.view.book

import com.team.domain.model.Book
import com.team.domain.model.exception.TodokTodokExceptions

sealed class SelectBookUiEvent {
    data object HideKeyboard : SelectBookUiEvent()

    data class NavigateToCreateDiscussionRoom(
        val book: Book,
    ) : SelectBookUiEvent()

    data class NavigateToDraftDiscussionRoom(
        val book: Book,
    ) : SelectBookUiEvent()

    data class ShowErrorMessage(
        val message: SelectBookErrorType,
    ) : SelectBookUiEvent()

    data class ShowNetworkErrorMessage(
        val exception: TodokTodokExceptions,
    ) : SelectBookUiEvent()

    data class ShowSearchedBookResultIsEmpty(
        val keyword: String,
    ) : SelectBookUiEvent()
}
