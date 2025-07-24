package com.example.todoktodok.data.datasource

import com.example.domain.model.Book
import com.example.todoktodok.data.network.request.SaveBookRequest
import com.example.todoktodok.data.network.response.BookResponse
import com.example.todoktodok.data.network.service.LibraryService

class RemoteBookDataSource(
    private val libraryService: LibraryService,
) : BookDataSource {
    override suspend fun fetchBooks(): List<Book> =
        libraryService.fetchBooks(token).map { bookResponse: BookResponse ->
            bookResponse.toDomain()
        }

    override suspend fun fetchBooks(searchInput: String): List<Book> {
        TODO("Not yet implemented")
    }

    override suspend fun saveBook(bookId: Long) {
        libraryService.saveBook(token, SaveBookRequest(bookId))
    }

    companion object {
        val token = "Bearer"
    }
}
