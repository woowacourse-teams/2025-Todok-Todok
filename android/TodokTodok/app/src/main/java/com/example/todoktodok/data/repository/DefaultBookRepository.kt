package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.RemoteBookDataSource

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
) : BookRepository {
    override suspend fun getBooks(searchInput: String): List<Book> = remoteBookDataSource.fetchBooks(searchInput)

    override suspend fun saveBook(book: Book) {
        remoteBookDataSource.saveBook(book.id)
    }
}
