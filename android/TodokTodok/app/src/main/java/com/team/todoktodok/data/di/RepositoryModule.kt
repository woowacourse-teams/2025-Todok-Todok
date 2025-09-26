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

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val discussionRepository: DiscussionRepository by lazy {
        DefaultDiscussionRepository(
            dataSourceModule.discussionRemoteDataSource,
            dataSourceModule.discussionLocalDataSource,
        )
    }

    val commentRepository: CommentRepository by lazy { DefaultCommentRepository(dataSourceModule.commentRemoteDataSource) }

    val memberRepository: MemberRepository by lazy { DefaultMemberRepository(dataSourceModule.memberRemoteDataSource) }

    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookRemoteDataSource) }

    val tokenRepository: TokenRepository by lazy { DefaultTokenRepository(dataSourceModule.tokenLocalDataSource) }

    val replyRepository: ReplyRepository by lazy { DefaultReplyRepository(dataSourceModule.replyRemoteDataSource) }

    val notificationRepository: NotificationRepository by lazy {
        DefaultNotificationRepository(
            dataSourceModule.notificationRemoteDataSource,
            dataSourceModule.notificationLocalDataSource,
        )
    }
}
