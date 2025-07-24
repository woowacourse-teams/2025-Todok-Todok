package com.example.todoktodok.presentation.view.note

import com.example.domain.model.Book

data class NoteState(
    val snap: String = INITIALIZE_STING_VALUE,
    val memo: String = INITIALIZE_STING_VALUE,
    val savedBooks: List<Book>? = null,
    val selectedBook: Book? = null,
) {
    fun modifySelectedBook(index: Int): NoteState {
        val selectedBook =
            savedBooks?.get(index) ?: throw IllegalArgumentException(CANNOT_FOUND_BOOK)
        return this.copy(selectedBook = selectedBook)
    }

    companion object {
        private const val NOT_EXIST_SELECT_BOOK = -1L
        private const val INITIALIZE_STING_VALUE = ""
        private const val CANNOT_FOUND_BOOK = "선택된 책을 찾을 수 없습니다."
    }
}
