package com.team.todoktodok.fake.datasource

import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.map
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.SearchedBookResponse
import com.team.todoktodok.fixture.SearchedBooksFixtures

class StubBookRemoteDataSource : BookRemoteDataSource {
    private val books =
        SearchedBooksFixtures.books.map { book: SearchedBook ->
            SearchedBookResponse(
                isbn = book.isbn,
                title = book.mainTitle,
                author = book.author,
                image = book.image,
            )
        }

    var callCount = 0
    var shouldFailFetchBooks = false
    var isInvalidKeyword = false

    override suspend fun fetchBooks(keyword: String): NetworkResult<List<SearchedBookResponse>> {
        callCount++
        if (shouldFailFetchBooks) return NetworkResult.Failure(TodokTodokExceptions.ConnectException)
        if (isInvalidKeyword) return NetworkResult.Success(emptyList<SearchedBookResponse>())
        return NetworkResult.Success(
            books,
        )
    }

    override suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long> {
        TODO("Not yet implemented")
    }
}
