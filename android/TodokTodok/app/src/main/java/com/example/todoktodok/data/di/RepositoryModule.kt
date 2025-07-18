package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.repository.BookRepositoryImpl
import kotlin.getValue

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val bookRepository: BookRepository by lazy { BookRepositoryImpl(dataSourceModule.BookDataSource) }
}
