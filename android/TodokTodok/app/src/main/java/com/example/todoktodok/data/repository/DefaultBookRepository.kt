package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.book.RemoteBookDataSource

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
) : BookRepository {
    override suspend fun getBooks(): List<Book> = remoteBookDataSource.fetchBooks()
}
