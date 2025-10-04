package com.team.todoktodok.presentation.compose.my.books

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface MyBooksUiEvent {
    data class ShowErrorMessage(
        val exceptions: TodokTodokExceptions,
    ) : MyBooksUiEvent
}
