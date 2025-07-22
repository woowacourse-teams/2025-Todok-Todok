package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.BookDataSource
import com.example.todoktodok.data.datasource.NoteDataSource
import com.example.todoktodok.data.datasource.RemoteNoteDataSource

class DataSourceModule {
    val bookDataSource: BookDataSource by lazy { BookDataSource() }

    val noteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }
}
