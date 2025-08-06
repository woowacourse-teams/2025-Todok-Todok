package com.team.todoktodok.data.di

import android.content.Context
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.datasource.book.DefaultBookRemoteDataSource
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.data.datasource.comment.DefaultCommentRemoteDataSource
import com.team.todoktodok.data.datasource.discussion.DefaultDiscussionRemoteDataSource
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.datasource.member.DefaultMemberRemoteDataSource
import com.team.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.team.todoktodok.data.datasource.token.TokenDataSource

class DataSourceModule(
    serviceModule: ServiceModule,
    context: Context,
) {
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

    val memberRemoteDataSource: MemberRemoteDataSource by lazy {
        DefaultMemberRemoteDataSource(
            serviceModule.memberService,
            tokenDataSource,
        )
    }

    val bookRemoteDataSource: BookRemoteDataSource by lazy { DefaultBookRemoteDataSource(serviceModule.bookService) }
}
