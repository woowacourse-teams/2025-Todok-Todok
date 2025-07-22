package com.example.todoktodok.data.datasource.book

import com.example.domain.model.Book

interface BookDataSource {
    suspend fun fetchBooks(): List<Book>
}
