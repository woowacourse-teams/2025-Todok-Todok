package com.team.todoktodok.data.datasource.book

import com.team.todoktodok.data.network.response.discussion.BookResponse

interface BookRemoteDataSource {
    suspend fun fetchBooks(keyword: String): List<BookResponse>
}
