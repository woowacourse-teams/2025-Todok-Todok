package com.team.domain.model.book

import com.team.domain.model.exception.ISBNException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ISBNTest {
    @ParameterizedTest
    @ValueSource(strings = ["1234567891234"])
    fun `도서 ISBDN의 자리수가 13자리인 숫자인 경우 정상적으로 생성된다`(value: String) {
        // when & then
        assertDoesNotThrow { ISBN(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["123456789123", "12345678912345"])
    fun `도서 ISBN의 자리수가 13자리가 아닐 경우 예외가 발생한다`(value: String) {
        // given, when
        val exception = assertThrows<IllegalArgumentException> { ISBN(value) }

        // then
        assertEquals(ISBNException.InvalidLength.message, exception.message)
    }

    @ParameterizedTest
    @ValueSource(strings = ["1a34567891234", "1ㄱ34567891234"])
    fun `도서 ISBN이 숫자가 아닐 경우 예외가 발생한다`(value: String) {
        // given, when
        val exception = assertThrows<IllegalArgumentException> { ISBN(value) }

        // than
        assertEquals(ISBNException.InvalidFormat.message, exception.message)
    }
}
