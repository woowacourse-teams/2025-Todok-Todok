package com.team.domain.model.book

data class SearchedBooksResult(
    val books: SearchedBooks,
    val hasNext: Boolean,
    val totalSize: SearchedBooksTotalSize,
) {
    companion object {
        fun SearchedBooksResult(
            books: SearchedBooks,
            hasNext: Boolean,
            totalSize: Int,
        ): SearchedBooksResult =
            SearchedBooksResult(
                books = books,
                hasNext = hasNext,
                totalSize = SearchedBooksTotalSize(totalSize),
            )
    }
}
