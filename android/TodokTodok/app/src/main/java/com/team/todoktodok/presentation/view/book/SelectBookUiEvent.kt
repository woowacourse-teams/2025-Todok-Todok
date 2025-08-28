package com.team.todoktodok.presentation.view.book

import com.team.domain.model.Book
import com.team.domain.model.exception.TodokTodokExceptions

sealed class SelectBookUiEvent {
    data class NavigateToCreateDiscussionRoom(
        val book: Book,
    ) : SelectBookUiEvent()

    data class ShowError(
        val message: SelectBookErrorType,
    ) : SelectBookUiEvent()

    data class ShowTodokTodokException(
        val exception: TodokTodokExceptions,
    ) : SelectBookUiEvent()
}
