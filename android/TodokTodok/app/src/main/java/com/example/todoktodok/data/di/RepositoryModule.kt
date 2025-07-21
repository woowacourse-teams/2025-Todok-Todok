package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.repository.DefaultBookRepository
import kotlin.getValue

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookDataSource) }
}
