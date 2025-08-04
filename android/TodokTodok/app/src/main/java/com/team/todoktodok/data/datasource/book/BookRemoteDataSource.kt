package com.team.todoktodok.data.datasource.book

import com.team.domain.model.Books

interface BookRemoteDataSource {
    suspend fun fetchBooks(keyword: String): Books
}
