package com.team.todoktodok.fake.datasource

import com.team.domain.model.Book
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.fixture.BOOKS_FIXTURES

class FakeBookRemoteDataSource(
    private val books: List<Book> = BOOKS_FIXTURES,
) : BookRemoteDataSource {
    val savedBookIds = mutableListOf<Long>()

    override suspend fun fetchBooks(): List<Book> = books

    override suspend fun fetchBooks(searchInput: String): List<Book> = books.filter { it.title.contains(searchInput, ignoreCase = true) }

    override suspend fun saveBook(bookId: Long) {
        savedBookIds.add(bookId)
    }

    override suspend fun saveSelectedBook(book: Book): Long {
        TODO("Not yet implemented")
    }
}
