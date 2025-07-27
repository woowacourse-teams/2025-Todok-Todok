package com.team.todoktodok.data.datasource.book

import com.team.domain.model.Book
import com.team.todoktodok.data.network.request.SaveBookRequest
import com.team.todoktodok.data.network.response.BookResponse
import com.team.todoktodok.data.network.service.BookService
import com.team.todoktodok.data.network.service.LibraryService

class DefaultBookRemoteDataSource(
    private val libraryService: LibraryService,
    private val bookService: BookService,
) : BookRemoteDataSource {
    override suspend fun fetchBooks(): List<Book> =
        libraryService.fetchBooks().map { bookResponse: BookResponse ->
            bookResponse.toDomain()
        }

    override suspend fun fetchBooks(searchInput: String): List<Book> =
        bookService
            .fetchBooks(searchInput)
            .map { bookResponse: BookResponse -> bookResponse.toDomain() }

    override suspend fun saveBook(bookId: Long) {
        libraryService.saveBook(SaveBookRequest(bookId))
    }
}
