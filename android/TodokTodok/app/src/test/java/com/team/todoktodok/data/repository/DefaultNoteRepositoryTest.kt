package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.repository.NoteRepository
import com.team.todoktodok.data.network.request.NoteRequest
import com.team.todoktodok.fake.datasource.FakeNoteDataSource
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

            // when
            repository.saveNote(book.id, "snap", "memo")

            // then
            assertEquals(1, fakeDataSource.savedRequests.size)

            val expected = NoteRequest(book.id, "snap", "memo")
            assertEquals(expected, fakeDataSource.savedRequests[0])
        }
}
