package com.team.domain.model.book

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SearchedBooksTotalSizeTest {
    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 15, 100, 199, 200])
    fun `검색된 총 책의 개수는 0에서 부터 200까지이다`(size: Int) {
        assertDoesNotThrow { SearchedBooksTotalSize(size) }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 201])
    fun `검색된 총 책의 개수가 0~200가 아니면 에러가 난다`(size: Int) {
        val exception =
            assertThrows<IllegalArgumentException> { SearchedBooksTotalSize(size) }
        assertEquals("[ERROR] 책 검색 요청시 총 책의 개수는 1에서 200까지만 가능합니다.", exception.message)
    }
}
