package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.BookDataSource

class DefaultBookRepository(
    private val remoteBookDataSource: BookDataSource,
) : BookRepository {
    override suspend fun searchBooks(searchInput: String): List<Book> = remoteBookDataSource.fetchBooks(searchInput)

    override suspend fun getBooks(): List<Book> = remoteBookDataSource.fetchBooks()

    override suspend fun saveBook(book: Book) {
        remoteBookDataSource.saveBook(book.id)
    }
}
