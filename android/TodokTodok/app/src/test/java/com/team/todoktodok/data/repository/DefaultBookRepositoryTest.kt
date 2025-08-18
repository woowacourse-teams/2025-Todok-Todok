package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Books
import com.team.todoktodok.data.datasource.book.BookRemoteDataSource
import com.team.todoktodok.fake.datasource.FakeBookRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun `키워드에 빈 값을 넣으면 빈 리스트를 반환한다`() =
        runTest {
            // given
            val keyword = ""
            val expected = Books(emptyList())

            // when
            val result = defaultBookRepository.fetchBooks(keyword)

            // then
            assertEquals(result, expected)
        }

    @Test
    fun `키워드가 유효하면 넣으면 리스트를 반환한다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val expected =
                Books(
                    listOf(
                        Book(
                            id = 5L,
                            title = "오브젝트",
                            author = "조영호",
                            image = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
                        ),
                        Book(
                            id = 8L,
                            title = "엘레강트 오브젝트",
                            author = "야코브 지걸",
                            image = "https://image.aladin.co.kr/product/25837/40/cover500/k762736538_1.jpg",
                        ),
                    ),
                )

            // when
            val result = defaultBookRepository.fetchBooks(keyword)

            // then
            assertEquals(result, expected)
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
