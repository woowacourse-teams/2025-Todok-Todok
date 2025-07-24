package com.example.domain.repository

import com.example.domain.model.Books

interface BookRepository {
    suspend fun getBooks(): Books
}
