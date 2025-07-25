package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRepository
import com.example.domain.repository.MemberRepository
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.repository.DefaultBookRepository
import com.example.todoktodok.data.repository.DefaultCommentRepository
import com.example.todoktodok.data.repository.DefaultDiscussionRepository
import com.example.todoktodok.data.repository.DefaultMemberRepository
import com.example.todoktodok.data.repository.DefaultNoteRepository

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val discussionRepository: DiscussionRepository by lazy {
        DefaultDiscussionRepository(
            dataSourceModule.discussionRemoteDataSource,
        )
    }
    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookRemoteDataSource) }

    val noteRepository: NoteRepository by lazy { DefaultNoteRepository(dataSourceModule.noteRemoteDataSource) }

    val commentRepository: CommentRepository by lazy { DefaultCommentRepository(dataSourceModule.commentRemoteDataSource) }

    val memberRepository: MemberRepository by lazy { DefaultMemberRepository(dataSourceModule.memberRemoteDataSource) }
}
