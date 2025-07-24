package com.example.todoktodok.fake.datasource

import com.example.domain.model.Book
import com.example.domain.model.Books
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.fixture.BOOKS_FIXTURES

class FakeBookDataSource(
    private val books: List<Book> = BOOKS_FIXTURES
) : BookDataSource {

    val savedBookIds = mutableListOf<Long>()

    override suspend fun fetchBooks(): List<Book> = books

    override suspend fun fetchBooks(searchInput: String): List<Book> =
        books.filter { it.title.contains(searchInput, ignoreCase = true) }

    override suspend fun saveBook(bookId: Long) {
        savedBookIds.add(bookId)
    }
}

