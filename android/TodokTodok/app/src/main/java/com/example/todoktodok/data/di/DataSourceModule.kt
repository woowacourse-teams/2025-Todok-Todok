package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.BookDataSource
import com.example.todoktodok.data.datasource.DefaultDiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource

class DataSourceModule {
    val bookDataSource: BookDataSource by lazy { BookDataSource() }

    val discussionRoomDataSource: DiscussionRoomDataSource by lazy { DefaultDiscussionRoomDataSource() }
}
