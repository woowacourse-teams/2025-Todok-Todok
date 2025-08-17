package com.team.todoktodok.data.datasource

import com.team.todoktodok.data.datasource.book.DefaultBookRemoteDataSource
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.service.BookService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultBookRemoteDataSourceTest {
    private lateinit var service: BookService
    private lateinit var dataSource: DefaultBookRemoteDataSource

    @BeforeEach
    fun setup() {
        service = mockk()
        dataSource = DefaultBookRemoteDataSource(service)
    }

    @Test
    fun `keyword가 주어졌을 때 책 목록을 가져온다`() =
        runTest {
            // given
            val keyword = "오브젝트"
            val expectedBooks =
                listOf(
                    BookResponse(
                        bookId = 5L,
                        bookTitle = "오브젝트",
                        bookAuthor = "조영호",
                        bookImage = "https://image.aladin.co.kr/product/19368/10/cover200/k972635015_1.jpg",
                    ),
                )
            coEvery { service.fetchBooks(keyword) } returns expectedBooks

            // when
            val result = dataSource.fetchBooks(keyword)

            // then
            assertEquals(expectedBooks, result)
            coVerify(exactly = 1) { service.fetchBooks(keyword) }
        }

    @Test
    fun `keyword가 비어있으면, 빈 리스트를 반환한다`() =
        runTest {
            // given
            val keyword = ""
            val expectedBooks = emptyList<BookResponse>()
            coEvery { service.fetchBooks(keyword) } returns emptyList()

            // when
            val result = dataSource.fetchBooks(keyword)

            // then
            assertEquals(expectedBooks, result)
            coVerify(exactly = 1) { service.fetchBooks(keyword) }
        }
}
