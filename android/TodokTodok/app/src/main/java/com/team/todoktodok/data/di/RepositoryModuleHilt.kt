package com.team.todoktodok.data.di

import com.team.domain.repository.BookRepository
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.domain.repository.ReplyRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.data.repository.DefaultBookRepository
import com.team.todoktodok.data.repository.DefaultCommentRepository
import com.team.todoktodok.data.repository.DefaultDiscussionRepository
import com.team.todoktodok.data.repository.DefaultMemberRepository
import com.team.todoktodok.data.repository.DefaultNotificationRepository
import com.team.todoktodok.data.repository.DefaultReplyRepository
import com.team.todoktodok.data.repository.DefaultTokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModuleHilt {
    @Binds
    abstract fun bindsDiscussionRepository(repository: DefaultDiscussionRepository): DiscussionRepository

    @Binds
    abstract fun bindsCommentRepository(repository: DefaultCommentRepository): CommentRepository

    @Binds
    abstract fun bindsMemberRepository(repository: DefaultMemberRepository): MemberRepository

    @Binds
    abstract fun bindsBookRepository(repository: DefaultBookRepository): BookRepository

    @Binds
    abstract fun binsTokenRepository(repository: DefaultTokenRepository): TokenRepository

    @Binds
    abstract fun bindsReplyRepository(repository: DefaultReplyRepository): ReplyRepository

    @Binds
    abstract fun bindsNotificationRepository(repository: DefaultNotificationRepository): NotificationRepository
}
