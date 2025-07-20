package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.data.repository.BookRepositoryImpl
import com.example.todoktodok.data.repository.DiscussionRoomRepositoryImpl
import kotlin.getValue

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val bookRepository: BookRepository by lazy { BookRepositoryImpl(dataSourceModule.bookDataSource) }

    val discussionRoomRepository: DiscussionRoomRepository by lazy {
        DiscussionRoomRepositoryImpl(
            dataSourceModule.discussionRoomDataSource
        )
    }
}
