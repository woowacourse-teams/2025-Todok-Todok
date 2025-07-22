package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.DiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSourceImpl
import com.example.todoktodok.data.datasource.book.RemoteBookDataSource
import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.datasource.note.RemoteNoteDataSource

class DataSourceModule {
    val remoteBookDataSource: RemoteBookDataSource by lazy { RemoteBookDataSource() }

    val noteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }

    val discussionRoomDataSource: DiscussionRoomDataSource by lazy { DiscussionRoomDataSourceImpl() }
}
