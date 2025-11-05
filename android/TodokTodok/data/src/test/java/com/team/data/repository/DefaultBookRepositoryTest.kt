package com.team.data.repository

import com.team.data.fake.datasource.FakeBookRemoteDataSource
import com.team.domain.model.book.Keyword
import com.team.domain.model.exception.NetworkResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DefaultBookRepositoryTest {
    @Test
    fun `도서 검색 결과가 없는 빈 리스트도 정상 매핑된다`() =
        runTest {
            val bookRemoteDataSource = FakeBookRemoteDataSource()
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
