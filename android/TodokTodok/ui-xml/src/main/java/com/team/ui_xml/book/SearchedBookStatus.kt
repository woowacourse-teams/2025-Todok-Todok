package com.team.ui_xml.book

sealed interface SearchedBookStatus {
    data object Loading : SearchedBookStatus

    data object NotStarted : SearchedBookStatus

    data object NotFound : SearchedBookStatus

    data object Success : SearchedBookStatus
}
