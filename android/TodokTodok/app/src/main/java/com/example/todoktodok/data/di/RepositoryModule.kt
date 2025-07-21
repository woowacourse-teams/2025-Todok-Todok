package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.data.repository.DefaultBookRepository
import com.example.todoktodok.data.repository.DefaultDiscussionRoomRepository
import kotlin.getValue

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookDataSource) }

    val discussionRoomRepository: DiscussionRoomRepository by lazy {
        DefaultDiscussionRoomRepository(
            dataSourceModule.discussionRoomDataSource,
        )
    }
}
