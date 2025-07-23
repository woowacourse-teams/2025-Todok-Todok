package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.DefaultDiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.data.datasource.book.RemoteBookDataSource
import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.datasource.note.RemoteNoteDataSource

class DataSourceModule {
    val remoteBookDataSource: BookDataSource by lazy { RemoteBookDataSource() }

    val remoteNoteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }

    val discussionRoomDataSource: DiscussionRoomDataSource by lazy { DefaultDiscussionRoomDataSource() }
}
