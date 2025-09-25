package com.team.todoktodok.data.repository

import com.team.domain.model.book.Keyword
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.fake.datasource.StubBookRemoteDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DefaultBookRepositoryTest {
    @Test
    fun `원격 실패면 실패가 그대로 전파된다`() =
        runTest {
            // given
            val bookRemoteDataSource = StubBookRemoteDataSource()
            val defaultBookRepository = DefaultBookRepository(bookRemoteDataSource)
            bookRemoteDataSource.shouldFailFetchBooks = true
            "오브젝트"

            val result =
                defaultBookRepository.fetchBooks(
                    size = 20,
                    keyword = Keyword("오브젝트"),
                )

            // then
            assertTrue(result is NetworkResult.Failure)
        }

    @Test
    fun `도서 검색 결과가 없는 빈 리스트도 정상 매핑된다`() =
        runTest {
            val bookRemoteDataSource = StubBookRemoteDataSource()
            val defaultBookRepository = DefaultBookRepository(bookRemoteDataSource)
            "ㅁ나ㅣㅇ러;ㅣ마넝리ㅏ;ㅁ넝리;ㅏㅓㅁㄴㄹㅇ"

            bookRemoteDataSource.isInvalidKeyword = true
            val result =
                defaultBookRepository.fetchBooks(
                    size = 20,
                    keyword = Keyword("오브젝트"),
                )

            assertAll(
                { assertTrue(result is NetworkResult.Success) },
                { assertTrue((result as NetworkResult.Success).data.books.isEmpty()) },
            )
        }
}
