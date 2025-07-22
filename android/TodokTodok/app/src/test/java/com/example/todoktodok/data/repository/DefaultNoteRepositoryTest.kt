package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.model.Note
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.data.network.request.toRequest
import com.example.todoktodok.fake.datasource.FakeNoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultNoteRepositoryTest {
    private lateinit var fakeDataSource: FakeNoteDataSource
    private lateinit var repository: NoteRepository

    @BeforeEach
    fun setUp() {
        fakeDataSource = FakeNoteDataSource()
        repository = DefaultNoteRepository(fakeDataSource)
    }

    @Test
    fun `새로운 기록을 저장한다`() =
        runTest {
            // given
            val book = Book(0, "안드로이드 프로그래밍", "페토", "")
            val note = Note(id = 0, snap = "새로운 기록", book = book, memo = "")

            // when
            repository.saveNote(note)

            // then
            assertEquals(1, fakeDataSource.savedRequests.size)
            assertEquals(note.toRequest(), fakeDataSource.savedRequests[0])
        }
}
