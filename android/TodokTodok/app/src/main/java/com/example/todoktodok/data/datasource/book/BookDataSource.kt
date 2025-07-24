package com.example.todoktodok.data.datasource.book

import com.example.domain.model.Books

interface BookDataSource {
    suspend fun fetchBooks(): Books
}
