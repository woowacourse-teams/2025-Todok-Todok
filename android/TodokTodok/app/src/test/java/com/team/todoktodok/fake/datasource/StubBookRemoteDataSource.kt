package com.team.todoktodok.fake.datasource

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.map
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.AladinBookResponse
import com.team.todoktodok.fixture.AladinBookFixtures

class StubBookRemoteDataSource : BookRemoteDataSource {
    private val books =
        AladinBookFixtures.books.map { book: AladinBook ->
            AladinBookResponse(
                isbn = book.isbn,
                title = book.mainTitle,
                author = book.author,
                image = book.image,
            )
        }

    var callCount = 0
    var shouldFailFetchBooks = false

    override suspend fun fetchBooks(keyword: String): NetworkResult<List<AladinBookResponse>> {
        callCount++
        if (shouldFailFetchBooks) return NetworkResult.Failure(TodokTodokExceptions.ConnectException)
        if (keyword.isBlank()) return NetworkResult.Success(emptyList<AladinBookResponse>())
        return NetworkResult.Success(
            books,
        )
    }

    override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> {
        TODO("Not yet implemented")
    }
}
