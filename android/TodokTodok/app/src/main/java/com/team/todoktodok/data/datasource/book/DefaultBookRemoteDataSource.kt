package com.team.todoktodok.data.datasource.book

import android.util.Log
import com.team.domain.model.Book
import com.team.todoktodok.data.network.request.CreateBookRequest
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

    override suspend fun saveSelectedBook(book: Book): Long {
        val response = libraryService.createBook(
            CreateBookRequest(
                bookIsbn = book.id,
                bookAuthor = book.author,
                bookImage = book.image,
                bookTitle = book.title
            )
        )
        if (!response.isSuccessful) {
            throw IllegalStateException("서버 응답이 실패했습니다")
        }

        val body = response.body()
        if (body == null) {
            throw IllegalStateException("서버 응답은 성공했지만 데이터가 없습니다")
        }

        return body
    }
}
