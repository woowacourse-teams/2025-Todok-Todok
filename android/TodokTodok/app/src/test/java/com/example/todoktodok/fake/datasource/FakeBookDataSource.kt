package com.example.todoktodok.fake.datasource

import com.example.domain.model.Books
import com.example.todoktodok.fixture.BOOKS_FIXTURES

class FakeBookDataSource : BookDataSource {
    override suspend fun fetchBooks(): Books = BOOKS_FIXTURES
}
