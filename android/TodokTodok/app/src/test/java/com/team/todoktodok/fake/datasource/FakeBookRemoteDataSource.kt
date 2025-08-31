package com.team.todoktodok.fake.datasource

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.map
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.fixture.AladinBookFixtures

class FakeBookRemoteDataSource : BookRemoteDataSource {
    private val books =
        AladinBookFixtures.books.map { book: AladinBook ->
            BookResponse(
                bookId = book.isbn,
                bookTitle = book.mainTitle,
                bookAuthor = book.author,
                bookImage = book.image,
            )
        }

    var shouldFailFetchBooks = false

    override suspend fun fetchBooks(keyword: String): NetworkResult<List<BookResponse>> {
        if (shouldFailFetchBooks) error("에러 발생")
        if (keyword.isBlank()) return NetworkResult.Success(emptyList<BookResponse>())
        return NetworkResult.Success(
            books,
        )
    }

    override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> {
        TODO("Not yet implemented")
    }
}
