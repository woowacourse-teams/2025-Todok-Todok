package com.team.todoktodok.data.repository

import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.fake.datasource.StubBookRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DefaultBookRepositoryTest {
    @Test
    fun `도서를 불러오면 도메인으로 매핑된다`() =
        runTest {
            val bookRemoteDataSource = StubBookRemoteDataSource()
            val defaultBookRepository = DefaultBookRepository(bookRemoteDataSource)
            val keyword = "오브젝트"

            val result = defaultBookRepository.fetchBooks(keyword)

            assertAll(
                { assertEquals(1, bookRemoteDataSource.callCount) },
                { assertTrue(result is NetworkResult.Success) },
                { assertEquals(16, (result as NetworkResult.Success).data.size) },
                { assertTrue((result as NetworkResult.Success).data is AladinBooks) },
                { assertTrue((result as NetworkResult.Success).data[0] is AladinBook) },
            )
        }

    @Test
    fun `원격 실패면 실패가 그대로 전파된다`() =
        runTest {
            // given
            val bookRemoteDataSource = StubBookRemoteDataSource()
            val defaultBookRepository = DefaultBookRepository(bookRemoteDataSource)
            bookRemoteDataSource.shouldFailFetchBooks = true
            val keyword = "오브젝트"

            val result = defaultBookRepository.fetchBooks(keyword)

            // then
            assertTrue(result is NetworkResult.Failure)
        }

    @Test
    fun `빈 리스트도 정상 매핑된다`() =
        runTest {
            val bookRemoteDataSource = StubBookRemoteDataSource()
            val defaultBookRepository = DefaultBookRepository(bookRemoteDataSource)
            val keyword = ""

            val result = defaultBookRepository.fetchBooks(keyword)

            assertAll(
                { assertTrue(result is NetworkResult.Success) },
                { assertTrue((result as NetworkResult.Success).data.isEmpty()) },
            )
        }
}
