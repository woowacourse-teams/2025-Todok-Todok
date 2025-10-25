package com.team.todoktodok.di

import android.content.Context
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
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import com.team.todoktodok.data.local.discussion.DiscussionDatabase
import com.team.todoktodok.data.network.service.BookService
import com.team.todoktodok.data.network.service.CommentService
import com.team.todoktodok.data.network.service.DiscussionService
import com.team.todoktodok.data.network.service.MemberService
import com.team.todoktodok.data.network.service.NotificationService
import com.team.todoktodok.data.network.service.ReplyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModuleHilt {
    @Provides
    @Singleton
    fun provideDiscussionRemoteDataSource(service: DiscussionService): DiscussionRemoteDataSource =
        DefaultDiscussionRemoteDataSource(service)

    @Provides
    @Singleton
    fun provideCommentRemoteDataSource(service: CommentService): CommentRemoteDataSource =
        DefaultCommentRemoteDataSource(
            service,
        )

    @Provides
    @Singleton
    fun provideTokenLocalDataSource(
        @ApplicationContext context: Context,
    ): TokenDataSource = TokenLocalDataSource(context)

    @Provides
    @Singleton
    fun provideMemberRemoteDataSource(
        service: MemberService,
        tokenLocalDataSource: TokenLocalDataSource,
    ): MemberRemoteDataSource =
        DefaultMemberRemoteDataSource(
            service,
            tokenLocalDataSource,
        )

    @Provides
    @Singleton
    fun provideBookRemoteDataSource(service: BookService): BookRemoteDataSource =
        DefaultBookRemoteDataSource(
            service,
        )

    @Provides
    @Singleton
    fun provideReplyRemoteDataSource(service: ReplyService): ReplyRemoteDataSource =
        DefaultReplyRemoteDataSource(
            service,
        )

    @Provides
    @Singleton
    fun provideDiscussionLocalDataSource(
        @ApplicationContext context: Context,
    ): DiscussionLocalDataSource =
        DefaultDiscussionLocalDataSource(
            DiscussionDatabase
                .getInstance(context)
                .discussionDao(),
        )

    @Provides
    @Singleton
    fun provideNotificationRemoteDataSource(service: NotificationService): NotificationRemoteDataSource =
        DefaultNotificationRemoteDataSource(
            service,
        )

    @Provides
    @Singleton
    fun provideNotificationLocalDataSource(
        @ApplicationContext context: Context,
    ): NotificationLocalDataSource = DefaultNotificationLocalDataSource(context)
}
