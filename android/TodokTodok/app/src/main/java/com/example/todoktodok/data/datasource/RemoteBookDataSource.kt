package com.example.todoktodok.data.datasource

import android.util.Log
import com.example.domain.model.Book
import com.example.todoktodok.data.network.request.SaveBookRequest
import com.example.todoktodok.data.network.response.BookResponse
import com.example.todoktodok.data.network.service.BookService
import com.example.todoktodok.data.network.service.LibraryService

class RemoteBookDataSource(
    private val libraryService: LibraryService,
    private val bookService: BookService,
) : BookDataSource {
    override suspend fun fetchBooks(): List<Book> =
        libraryService.fetchBooks(token).map { bookResponse: BookResponse ->
            bookResponse.toDomain()
        }

    override suspend fun fetchBooks(searchInput: String): List<Book> =
        bookService
            .fetchBooks(token, searchInput)
            .map { bookResponse: BookResponse -> bookResponse.toDomain() }

    override suspend fun saveBook(bookId: Long) {
        Log.d("test", "호출1")
        libraryService.saveBook(token, SaveBookRequest(bookId))
    }

    companion object {
        val token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3NTM5NDg1MDh9.cJCjodXTn0Tu5RrWt8fmgL4K9YZwQJpDrjgmnmz5OC0"
    }
}
