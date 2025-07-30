package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.repository.BookRepository
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource

class DefaultBookRepository(
    private val remoteBookRemoteDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun searchBooks(searchInput: String): List<Book> = remoteBookRemoteDataSource.fetchBooks(searchInput)

    override suspend fun getBooks(): List<Book> = remoteBookRemoteDataSource.fetchBooks()

    override suspend fun saveBook(book: Book) {
        remoteBookRemoteDataSource.saveBook(book.id)
    }
}
