package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.fake.datasource.FakeBookRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultBookRepositoryTest {
    private lateinit var fakeBookDataSource: FakeBookRemoteDataSource
    private lateinit var defaultBookRepository: BookRepository

    @BeforeEach
    fun setUp() {
        fakeBookDataSource =
            FakeBookRemoteDataSource(
                books =
                    listOf(
                        Book(
                            id = 1,
                            title = "테스트용 책",
                            author = "테스트 작가",
                            image = "https://example.com/test.jpg",
                        ),
                    ),
            )
        defaultBookRepository = DefaultBookRepository(fakeBookDataSource)
    }

    @Test
    fun `저장된 책을 가져온다`() =
        runTest {
            // given
            val expected =
                Book(
                    id = 1,
                    title = "테스트용 책",
                    author = "테스트 작가",
                    image = "https://example.com/test.jpg",
                )

            // when
            val actual = defaultBookRepository.getBooks()

            // then
            assertEquals(expected, actual.first())
        }

    @Test
    fun `책을 저장하면 ID가 기록된다`() =
        runTest {
            // when
            fakeBookDataSource.saveBook(100)

            // then
            assertTrue(fakeBookDataSource.savedBookIds.contains(100))
        }
}
