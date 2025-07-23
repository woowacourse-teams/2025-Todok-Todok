package com.example.todoktodok.data.datasource

import com.example.domain.model.Book

class RemoteBookDataSource : BookDataSource {
    override suspend fun fetchBooks(searchInput: String): List<Book> {
        TODO("Not yet implemented")
    }

    override suspend fun saveBook(bookId: Long) {
        TODO("Not yet implemented")
    }
}
