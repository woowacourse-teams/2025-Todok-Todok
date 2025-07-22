package com.example.todoktodok.fake.datasource

import com.example.domain.model.Book
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.fixture.BOOKS_FIXTURES

class FakeBookDataSource : BookDataSource {
    override suspend fun fetchBooks(): List<Book> = BOOKS_FIXTURES
}
