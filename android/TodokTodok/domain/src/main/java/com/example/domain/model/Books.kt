package com.example.domain.model

data class Books(
    val items: List<Book>,
) {
    fun findBookById(id: Long): Book = items.find { it.id == id } ?: throw IllegalArgumentException(CANNOT_FIND_BOOK)

    companion object {
        private const val CANNOT_FIND_BOOK = "해당하는 책을 찾을 수 없습니다."
    }
}
