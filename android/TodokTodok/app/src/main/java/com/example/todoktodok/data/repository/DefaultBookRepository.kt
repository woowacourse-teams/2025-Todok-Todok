package com.example.todoktodok.data.repository

import com.example.domain.model.Books
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.book.BookDataSource

class DefaultBookRepository(
    private val remoteBookDataSource: BookDataSource,
) : BookRepository {
    override suspend fun getBooks(): Books = remoteBookDataSource.fetchBooks()
}
