package com.team.domain.model.book

data class SearchedBooksResult(
    val books: SearchedBooks,
    val hasNext: Boolean,
    val _totalSize: SearchedBooksTotalSize,
) {
    val totalSize: Int
        get() = _totalSize.value

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
