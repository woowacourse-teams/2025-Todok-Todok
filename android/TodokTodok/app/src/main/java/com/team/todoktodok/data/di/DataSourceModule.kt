package com.team.todoktodok.data.di

import android.content.Context
import androidx.room.Room
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.data.datasource.book.DefaultBookRemoteDataSource
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.data.datasource.comment.DefaultCommentRemoteDataSource
import com.team.todoktodok.data.datasource.discussion.DefaultDiscussionLocalDataSource
import com.team.todoktodok.data.datasource.discussion.DefaultDiscussionRemoteDataSource
import com.team.todoktodok.data.datasource.discussion.DiscussionLocalDataSource
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.datasource.discussion.member.DefaultMemberRemoteDataSource
import com.team.todoktodok.data.datasource.discussion.member.MemberRemoteDataSource
import com.team.todoktodok.data.datasource.notification.DefaultNotificationLocalDataSource
import com.team.todoktodok.data.datasource.notification.DefaultNotificationRemoteDataSource
import com.team.todoktodok.data.datasource.notification.NotificationLocalDataSource
import com.team.todoktodok.data.datasource.notification.NotificationRemoteDataSource
import com.team.todoktodok.data.datasource.reply.DefaultReplyRemoteDataSource
import com.team.todoktodok.data.datasource.reply.ReplyRemoteDataSource
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import com.team.todoktodok.data.local.discussion.DiscussionDatabase

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

    val tokenLocalDataSource: TokenLocalDataSource by lazy { TokenLocalDataSource(context) }

    val memberRemoteDataSource: MemberRemoteDataSource by lazy {
        DefaultMemberRemoteDataSource(
            serviceModule.memberService,
            tokenLocalDataSource,
        )
    }

    val bookRemoteDataSource: BookRemoteDataSource by lazy {
        DefaultBookRemoteDataSource(
            serviceModule.bookService,
        )
    }

    val replyRemoteDataSource: ReplyRemoteDataSource by lazy {
        DefaultReplyRemoteDataSource(
            serviceModule.replyService,
        )
    }

    val discussionLocalDataSource: DiscussionLocalDataSource by lazy {
        DefaultDiscussionLocalDataSource(
            Room
                .databaseBuilder(
                    context,
                    DiscussionDatabase::class.java,
                    DATABASE_NAME,
                ).build()
                .discussionDao(),
        )
    }

    val notificationRemoteDataSource: NotificationRemoteDataSource by lazy {
        DefaultNotificationRemoteDataSource(
            serviceModule.notificationService,
        )
    }

    val notificationLocalDataSource: NotificationLocalDataSource by lazy {
        DefaultNotificationLocalDataSource(
            context,
        )
    }

    companion object {
        private const val DATABASE_NAME: String = "discussion"
    }
}
