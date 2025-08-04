package com.team.todoktodok.data.di

import com.team.domain.repository.BookRepository
import com.team.domain.repository.CommentRepository
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.data.repository.DefaultBookRepository
import com.team.todoktodok.data.repository.DefaultCommentRepository
import com.team.todoktodok.data.repository.DefaultDiscussionRepository
import com.team.todoktodok.data.repository.DefaultMemberRepository

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val discussionRepository: DiscussionRepository by lazy {
        DefaultDiscussionRepository(
            dataSourceModule.discussionRemoteDataSource,
        )
    }

    val commentRepository: CommentRepository by lazy { DefaultCommentRepository(dataSourceModule.commentRemoteDataSource) }

    val memberRepository: MemberRepository by lazy { DefaultMemberRepository(dataSourceModule.memberRemoteDataSource) }

    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookRemoteDataSource) }
}
