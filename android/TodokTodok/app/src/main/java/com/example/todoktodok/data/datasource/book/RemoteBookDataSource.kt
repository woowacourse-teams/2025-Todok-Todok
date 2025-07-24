package com.example.todoktodok.data.datasource.book

import com.example.domain.model.Book
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.data.network.request.SaveBookRequest
import com.example.todoktodok.data.network.response.BookResponse
import com.example.todoktodok.data.network.service.BookService
import com.example.todoktodok.data.network.service.LibraryService

class RemoteBookDataSource(
    private val libraryService: LibraryService,
    private val bookService: BookService,
) : BookDataSource {
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
