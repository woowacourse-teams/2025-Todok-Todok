package com.team.todoktodok.data.di

import com.team.todoktodok.data.network.service.BookService
import com.team.todoktodok.data.network.service.CommentService
import com.team.todoktodok.data.network.service.DiscussionService
import com.team.todoktodok.data.network.service.MemberService
import com.team.todoktodok.data.network.service.NotificationService
import com.team.todoktodok.data.network.service.RefreshService
import com.team.todoktodok.data.network.service.ReplyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModuleHilt {
    @Auth
    @Provides
    @Singleton
    fun provideDiscussionService(
        @Auth retrofit: Retrofit,
    ): DiscussionService = retrofit.create(DiscussionService::class.java)

    @Auth
    @Provides
    @Singleton
    fun provideMemberService(
        @Auth retrofit: Retrofit,
    ): MemberService = retrofit.create(MemberService::class.java)

    @Auth
    @Provides
    @Singleton
    fun provideCommentService(
        @Auth retrofit: Retrofit,
    ): CommentService = retrofit.create(CommentService::class.java)

    @Auth
    @Provides
    @Singleton
    fun provideBookService(
        @Auth retrofit: Retrofit,
    ): BookService = retrofit.create(BookService::class.java)

    @Auth
    @Provides
    @Singleton
    fun provideReplyService(
        @Auth retrofit: Retrofit,
    ): ReplyService = retrofit.create(ReplyService::class.java)

    @Client
    @Provides
    @Singleton
    fun provideRefreshService(
        @Client retrofit: Retrofit,
    ): RefreshService = retrofit.create(RefreshService::class.java)

    @Auth
    @Provides
    @Singleton
    fun provideNotificationService(
        @Auth retrofit: Retrofit,
    ): NotificationService = retrofit.create(NotificationService::class.java)
}
