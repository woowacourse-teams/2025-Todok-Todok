package com.team.todoktodok.presentation.view.book

import com.team.todoktodok.R

enum class SelectBookErrorType(
    val id: Int,
) {
    ERROR_NO_SELECTED_BOOK(R.string.error_no_selected_book),
    ERROR_EMPTY_KEYWORD(R.string.error_empty_keyword),
    ERROR_SAME_KEYWORD(R.string.error_same_keyword),
    ERROR_NETWORK(R.string.error_network),
}
