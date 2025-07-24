package com.example.todoktodok.data.di

import android.content.Context
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
import com.example.todoktodok.data.datasource.token.TokenDataSource

class DataSourceModule(
    serviceModule: ServiceModule,
    context: Context,
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

    val tokenDataSource: TokenDataSource by lazy { TokenDataSource(context) }

    val remoteMemberDataSource: MemberDataSource by lazy { RemoteMemberDataSource(serviceModule.memberService, tokenDataSource) }
}
