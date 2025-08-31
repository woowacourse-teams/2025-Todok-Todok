package com.team.todoktodok.data.repository

import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.fake.datasource.FakeBookRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultBookRepositoryTest {
    private lateinit var defaultBookRepository: DefaultBookRepository
    private lateinit var bookRemoteDataSource: BookRemoteDataSource

    @BeforeEach
    fun setUp() {
        bookRemoteDataSource = FakeBookRemoteDataSource()
        defaultBookRepository = DefaultBookRepository(bookRemoteDataSource)
    }

    @Test
    fun `fetchBooks에서 강제 에러 발생 시 예외를 던진다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val fake =
                FakeBookRemoteDataSource().apply {
                    shouldFailFetchBooks = true
                }

            // when
            val result = runCatching { fake.fetchBooks(keyword) }

            // then
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()?.message).isEqualTo("에러 발생")
        }
}
