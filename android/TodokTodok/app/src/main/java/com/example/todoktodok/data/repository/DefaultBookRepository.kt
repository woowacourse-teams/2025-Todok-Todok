package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.BookDataSource

class DefaultBookRepository(
    private val bookDataSource: BookDataSource,
) : BookRepository {
    override suspend fun getBooks(): List<Book> = bookDataSource.fetchBooks()
}
