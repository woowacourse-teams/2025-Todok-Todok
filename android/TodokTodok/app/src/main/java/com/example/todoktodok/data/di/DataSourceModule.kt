package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.BookDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSourceImpl

class DataSourceModule {
    val bookDataSource: BookDataSource by lazy { BookDataSource() }

    val discussionRoomDataSource: DiscussionRoomDataSource by lazy { DiscussionRoomDataSourceImpl() }
}
