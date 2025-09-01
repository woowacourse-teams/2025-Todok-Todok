package com.team.todoktodok.data.datasource.book

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.BookRequest
import com.team.todoktodok.data.network.response.book.AladinBookResponse

interface BookRemoteDataSource {
    suspend fun fetchBooks(keyword: String): NetworkResult<List<AladinBookResponse>>

    suspend fun saveBook(bookRequest: BookRequest): NetworkResult<Long>
}
