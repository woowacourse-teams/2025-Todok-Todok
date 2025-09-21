package com.team.domain.model.book

data class SearchedBooksResult(
    val books: SearchedBooks,
    val hasNext: Boolean,
    private val _totalSize: SearchedBooksTotalSize,
) {
    val totalSize: Int
        get() = _totalSize.value

    fun addSearchedBooks(
        books: SearchedBooks,
        hasNext: Boolean,
    ): SearchedBooksResult {
        val totalSearchedBooks = this.books + books
        return SearchedBooksResult(
            books = totalSearchedBooks,
            hasNext = hasNext,
            totalSize = _totalSize.value,
        )
    }

    companion object {
        fun SearchedBooksResult(
            books: SearchedBooks,
            hasNext: Boolean,
            totalSize: Int,
        ): SearchedBooksResult =
            SearchedBooksResult(
                books = books,
                hasNext = hasNext,
                _totalSize = SearchedBooksTotalSize(totalSize),
            )
    }
}
