package com.team.todoktodok.presentation.xml.book

import com.team.domain.model.book.AladinBook
import com.team.domain.model.exception.TodokTodokExceptions

sealed class SelectBookUiEvent {
    data class NavigateToCreateDiscussionRoom(
        val book: AladinBook,
    ) : SelectBookUiEvent()

    data class ShowException(
        val exception: TodokTodokExceptions,
    ) : SelectBookUiEvent()
}
