package com.team.todoktodok.data.repository

import com.team.domain.model.Books
import com.team.domain.repository.BookRepository
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource

class DefaultBookRepository(
    private val bookDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun fetchBooks(keyword: String): Books = bookDataSource.fetchBooks(keyword)
}
