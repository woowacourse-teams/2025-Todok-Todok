package com.example.todoktodok.data.repository

import com.example.domain.model.Book
import com.example.domain.repository.BookRepository
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.fake.datasource.FakeBookDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultBookRepositoryTest {
    private lateinit var fakeBookDataSource: BookDataSource
    private lateinit var defaultBookRepository: BookRepository

    @BeforeEach
    fun setUp() {
        fakeBookDataSource = FakeBookDataSource()
        defaultBookRepository = DefaultBookRepository(fakeBookDataSource)
    }

    @Test
    fun `저장된 책을 가져온다`() =
        runTest {
            // when
            val books =
                Book(
                    id = 1,
                    title = "나무처럼 생각하기: 자연과 함께 살아가는 법",
                    author = "피터 울렙",
                    image = "https://example.com/book1.jpg",
                )

            // given
            val expected = defaultBookRepository.getBooks()

            // then
            assertEquals(books, expected.items[0])
        }
}
