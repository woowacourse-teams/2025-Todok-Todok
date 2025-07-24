package com.example.todoktodok.data.di

import android.content.Context
import com.example.todoktodok.data.datasource.CommentDataSource
import com.example.todoktodok.data.datasource.DefaultCommentDataSource
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.data.datasource.book.RemoteBookDataSource
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
    val bookDataSource: BookDataSource by lazy { RemoteBookDataSource() }

    val noteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }

    val discussionRemoteDataSource: DiscussionRemoteDataSource by lazy {
        DefaultDiscussionRemoteDataSource(
            serviceModule.discussionService,
        )
    }

    val commentDataSource: CommentDataSource by lazy { DefaultCommentDataSource() }

    val tokenDataSource: TokenDataSource by lazy { TokenDataSource(context) }

    val remoteMemberDataSource: MemberDataSource by lazy { RemoteMemberDataSource(serviceModule.memberService, tokenDataSource) }
}
