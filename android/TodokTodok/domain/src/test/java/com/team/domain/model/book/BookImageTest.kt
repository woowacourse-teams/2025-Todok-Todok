package com.team.domain.model.book

import com.team.domain.model.exception.BookImageException
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class BookImageTest {
    @ParameterizedTest
    @ValueSource(strings = ["https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg"])
    fun `BookImage가 유효한 URL 형식이면 정상적으로 생성된다`(value: String) {
        // when & then
        assertDoesNotThrow { BookImage(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["https://image.todoktodok.co.kr/product/19368/10/coversum/k972635015_1.jpg"])
    fun `BookImage가 유효하지 않은 URL 형식이면 예외가 발생한다`(value: String) {
        // give & when
        val exception = assertThrows<IllegalArgumentException> { BookImage(value) }

        // then
        assertEquals(BookImageException.InvalidUrl.message, exception.message)
    }
}
