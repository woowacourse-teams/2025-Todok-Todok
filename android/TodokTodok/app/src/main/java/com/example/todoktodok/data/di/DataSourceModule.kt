package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.BookDataSource
import com.example.todoktodok.data.datasource.CommentDataSource
import com.example.todoktodok.data.datasource.DefaultCommentDataSource
import com.example.todoktodok.data.datasource.DefaultDiscussionRoomDataSource
import com.example.todoktodok.data.datasource.DiscussionRoomDataSource
import com.example.todoktodok.data.datasource.NoteDataSource
import com.example.todoktodok.data.datasource.RemoteNoteDataSource

class DataSourceModule(
    serviceModule: ServiceModule,
) {
    val bookDataSource: BookDataSource by lazy { BookDataSource() }

    val noteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }

    val discussionRoomDataSource: DiscussionRoomDataSource by lazy { DefaultDiscussionRoomDataSource() }

    val commentDataSource: CommentDataSource by lazy { DefaultCommentDataSource() }
}
