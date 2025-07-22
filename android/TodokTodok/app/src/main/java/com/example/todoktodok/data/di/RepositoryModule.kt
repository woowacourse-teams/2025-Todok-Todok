package com.example.todoktodok.data.di

import com.example.domain.repository.BookRepository
import com.example.domain.repository.DiscussionRoomRepository
import com.example.todoktodok.data.repository.DiscussionRoomRepositoryImpl
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.repository.DefaultBookRepository
import com.example.todoktodok.data.repository.DefaultNoteRepository
import kotlin.getValue

class RepositoryModule(
    dataSourceModule: DataSourceModule,
) {

    val discussionRoomRepository: DiscussionRoomRepository by lazy {
        DiscussionRoomRepositoryImpl(
            dataSourceModule.discussionRoomDataSource
        )
    }
    val bookRepository: BookRepository by lazy { DefaultBookRepository(dataSourceModule.bookDataSource) }

    val noteRepository: NoteRepository by lazy { DefaultNoteRepository(dataSourceModule.noteDataSource) }
}
