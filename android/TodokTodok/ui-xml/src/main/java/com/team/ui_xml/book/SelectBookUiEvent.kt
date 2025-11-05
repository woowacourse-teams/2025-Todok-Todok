package com.team.ui_xml.book

import com.team.domain.model.book.SearchedBook
import com.team.domain.model.exception.TodokTodokExceptions

sealed class SelectBookUiEvent {
    data class NavigateToCreateDiscussionRoom(
        val book: SearchedBook,
    ) : SelectBookUiEvent()

    data class ShowException(
        val exception: TodokTodokExceptions,
    ) : SelectBookUiEvent()
}
