package com.team.todoktodok.presentation.view.book

sealed interface SearchedBookResultStatus {
    data object Loading : SearchedBookResultStatus

    data object NotStarted : SearchedBookResultStatus

    data object NotFound : SearchedBookResultStatus

    data object Success : SearchedBookResultStatus
}
