package com.team.domain.model.book

import com.team.domain.model.exception.BookTitleException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BookTitleTest {
    @ParameterizedTest
    @ValueSource(strings = ["오브젝트", "코틀린 코루틴의 정석"])
    fun `도서 제목이 비어있지 않거나 공백이지 않는 경우 정상적으로 생성된다`(value: String) {
        // given & then
        assertDoesNotThrow { BookTitle(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "    ", "\n", "\t"])
    fun `도서 제목이 비었거나 공백일 경우 예외를 발생한다`(value: String) {
        // given, when
        val exception = assertThrows<IllegalArgumentException> { BookTitle(value) }

        // then
        assertEquals(BookTitleException.EmptyBookTitle.message, exception.message)
    }
}
