package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.domain.repository.CommentRepository
import com.example.domain.repository.DiscussionRoomRepository
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.repository.DefaultBookRepository
import com.example.todoktodok.data.repository.DefaultCommentRepository
import com.example.todoktodok.data.repository.DefaultDiscussionRoomRepository
import com.example.todoktodok.data.repository.DefaultNoteRepository

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {
    val discussionRoomRepository: DiscussionRoomRepository by lazy {
        DefaultDiscussionRoomRepository(
            dataSourceModule.discussionRoomDataSource,
        )
    }
    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookDataSource) }

    val noteRepository: NoteRepository by lazy { DefaultNoteRepository(dataSourceModule.noteDataSource) }

    val commentRepository: CommentRepository by lazy { DefaultCommentRepository(dataSourceModule.commentDataSource) }
}
