package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.BookDataSource

class DataSourceModule {
    val bookDataSource: BookDataSource by lazy { BookDataSource() }
}
