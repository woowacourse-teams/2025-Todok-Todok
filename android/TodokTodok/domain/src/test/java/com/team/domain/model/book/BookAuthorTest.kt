package com.team.domain.model.book

import com.team.domain.model.exception.BookAuthorException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BookAuthorTest {
    @ParameterizedTest
    @ValueSource(strings = ["조영호", "이동익", "마티아스 노박"])
    fun `책의 저자가 비어있거나 공백이 아닐 경우 정상적으로 생성된다`(author: String) {
        // when & then
        assertDoesNotThrow { BookAuthor(author) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "    ", "\n", "\t"])
    fun `책의 저자가 비어있거나 공백일 경우 예외가 발생한다`(author: String) {
        // given, when
        val exception = assertThrows<IllegalArgumentException> { BookAuthor(author) }

        // then
        assertEquals(BookAuthorException.EmptyBookAuthor.message, exception.message)
    }
}
