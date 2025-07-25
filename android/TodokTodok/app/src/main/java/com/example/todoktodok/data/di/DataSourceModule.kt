package com.example.todoktodok.data.di

import android.content.Context
import com.example.todoktodok.data.datasource.book.BookRemoteDataSource
import com.example.todoktodok.data.datasource.book.DefaultBookRemoteDataSource
import com.example.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.example.todoktodok.data.datasource.comment.DefaultCommentRemoteDataSource
import com.example.todoktodok.data.datasource.discussion.DefaultDiscussionRemoteDataSource
import com.example.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.example.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.example.todoktodok.data.datasource.member.DefaultMemberRemoteDataSource
import com.example.todoktodok.data.datasource.note.NoteRemoteDataSource
import com.example.todoktodok.data.datasource.note.DefaultNoteRemoteDataSource
import com.example.todoktodok.data.datasource.token.TokenDataSource

class DataSourceModule(
    serviceModule: ServiceModule,
    context: Context,
) {
    val defaultBookRemoteDataSource: BookRemoteDataSource by lazy {
        DefaultBookRemoteDataSource(
            serviceModule.libraryService,
            serviceModule.bookService,
        )
    }

    val noteRemoteDataSource: NoteRemoteDataSource by lazy { DefaultNoteRemoteDataSource(serviceModule.noteService) }

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

    val defaultMemberRemoteDataSource: MemberRemoteDataSource by lazy {
        DefaultMemberRemoteDataSource(
            serviceModule.memberService,
            tokenDataSource,
        )
    }
}
