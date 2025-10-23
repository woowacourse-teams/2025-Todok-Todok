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
    @Provides
    @Singleton
    fun provideDiscussionService(retrofit: Retrofit): DiscussionService = retrofit.create(DiscussionService::class.java)

    @Provides
    @Singleton
    fun provideMemberService(retrofit: Retrofit): MemberService = retrofit.create(MemberService::class.java)

    @Provides
    @Singleton
    fun provideCommentService(retrofit: Retrofit): CommentService = retrofit.create(CommentService::class.java)

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit): BookService = retrofit.create(BookService::class.java)

    @Provides
    @Singleton
    fun provideReplyService(retrofit: Retrofit): ReplyService = retrofit.create(ReplyService::class.java)

    @Provides
    @Singleton
    fun provideRefreshService(
        @Client retrofit: Retrofit,
    ): RefreshService = retrofit.create(RefreshService::class.java)

    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService = retrofit.create(NotificationService::class.java)
}
