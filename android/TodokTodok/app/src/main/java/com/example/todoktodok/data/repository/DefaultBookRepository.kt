package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.book.BookRemoteDataSource

class DefaultBookRepository(
    private val remoteBookRemoteDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun searchBooks(searchInput: String): List<Book> = remoteBookRemoteDataSource.fetchBooks(searchInput)

    override suspend fun getBooks(): List<Book> = remoteBookRemoteDataSource.fetchBooks()

    override suspend fun saveBook(book: Book) {
        remoteBookRemoteDataSource.saveBook(book.id)
    }
}
