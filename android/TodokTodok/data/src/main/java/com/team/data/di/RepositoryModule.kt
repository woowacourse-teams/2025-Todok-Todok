package com.team.data.di

import com.team.data.repository.DefaultBookRepository
import com.team.data.repository.DefaultCommentRepository
import com.team.data.repository.DefaultDiscussionRepository
import com.team.data.repository.DefaultMemberRepository
import com.team.data.repository.DefaultNotificationRepository
import com.team.data.repository.DefaultReplyRepository
import com.team.data.repository.DefaultTokenRepository
import com.team.domain.repository.BookRepository
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.domain.repository.ReplyRepository
import com.team.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsDiscussionRepository(repository: DefaultDiscussionRepository): DiscussionRepository

    @Binds
    @Singleton
    abstract fun bindsCommentRepository(repository: DefaultCommentRepository): CommentRepository

    @Binds
    @Singleton
    abstract fun bindsMemberRepository(repository: DefaultMemberRepository): MemberRepository

    @Binds
    @Singleton
    abstract fun bindsBookRepository(repository: DefaultBookRepository): BookRepository

    @Binds
    @Singleton
    abstract fun bindsTokenRepository(repository: DefaultTokenRepository): TokenRepository

    @Binds
    @Singleton
    abstract fun bindsReplyRepository(repository: DefaultReplyRepository): ReplyRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(repository: DefaultNotificationRepository): NotificationRepository
}
