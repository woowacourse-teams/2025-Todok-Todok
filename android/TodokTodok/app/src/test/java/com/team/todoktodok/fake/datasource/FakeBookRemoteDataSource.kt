package com.team.todoktodok.fake.datasource

import com.team.domain.model.Book
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.fixture.BOOKS

class FakeBookRemoteDataSource : BookRemoteDataSource {
    private val books =
        BOOKS.items.map { book: Book ->
            BookResponse(
                bookId = book.id,
                bookTitle = book.title,
                bookAuthor = book.author,
                bookImage = book.image,
            )
        }

    var shouldFailFetchBooks = false

    override suspend fun fetchBooks(keyword: String): NetworkResult<List<BookResponse>> {
        if (shouldFailFetchBooks) error("에러 발생")
        if (keyword.isBlank()) return NetworkResult.Success(emptyList<BookResponse>())
        return NetworkResult.Success(
            books.filter { bookResponse: BookResponse ->
                bookResponse.bookTitle.contains(keyword, ignoreCase = true) ||
                    bookResponse.bookAuthor.contains(keyword, ignoreCase = true)
            },
        )
    }

    override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> {
        TODO("Not yet implemented")
    }
}
