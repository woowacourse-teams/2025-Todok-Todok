package com.team.domain.model.book

import com.team.domain.model.exception.ISBNException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ISBNTest {
    @ParameterizedTest
    @ValueSource(longs = [1234567891234L])
    fun `도서 ISBN의 자리수가 13자리인 숫자인 경우 정상적으로 생성된다`(value: Long) {
        // when & then
        assertDoesNotThrow { ISBN(value) }
    }

    @ParameterizedTest
    @ValueSource(longs = [1234567891L])
    fun `도서 ISBN의 자리수가 10자리인 숫자인 경우 정상적으로 생성된다`(value: Long) {
        // when & then
        assertDoesNotThrow { ISBN(value) }
    }

    @ParameterizedTest
    @ValueSource(longs = [123456789L, 12345678912L, 123456789123L, 12345678912345L])
    fun `도서 ISBN의 자리수가 10자리 또는 13자리가 아닐 경우 예외가 발생한다`(value: Long) {
        // when
        val exception = assertThrows<IllegalArgumentException> { ISBN(value) }

        // then
        assertEquals(ISBNException.InvalidLength.message, exception.message)
    }
}
