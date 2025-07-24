package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.BookDataSource
import com.example.todoktodok.data.datasource.CommentRemoteDataSource
import com.example.todoktodok.data.datasource.DefaultCommentRemoteDataSource
import com.example.todoktodok.data.datasource.RemoteBookDataSource
import com.example.todoktodok.data.datasource.discussion.DefaultDiscussionRemoteDataSource
import com.example.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.example.todoktodok.data.datasource.member.MemberDataSource
import com.example.todoktodok.data.datasource.member.RemoteMemberDataSource
import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.datasource.note.RemoteNoteDataSource

class DataSourceModule(
    serviceModule: ServiceModule,
) {
    val remoteBookDataSource: BookDataSource by lazy {
        RemoteBookDataSource(
            serviceModule.libraryService,
            serviceModule.bookService,
        )
    }

    val noteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }

    val discussionRemoteDataSource: DiscussionRemoteDataSource by lazy {
        DefaultDiscussionRemoteDataSource(
            serviceModule.discussionService,
        )
    }

    val commentRemoteDataSource: CommentRemoteDataSource by lazy {
        DefaultCommentRemoteDataSource(
            serviceModule.commentService,
        )
    }

    val remoteMemberDataSource: MemberDataSource by lazy { RemoteMemberDataSource(serviceModule.memberService) }
}
