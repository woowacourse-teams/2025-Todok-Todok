package com.team.domain.model.book

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class KeywordTest {
    @Test
    fun `검색어에 공백을 입력하지 않고 1자 이상의 글자를 입력하면 정상적으로 생성된다`() {
        // given
        val value = "오브젝트"

        // when & then
        assertDoesNotThrow { Keyword(value) }
    }

    @Test
    fun `검색어를 입력하지 않으면 예외를 발생한다`() {
        // given
        val value = ""

        // when & then
        assertThrows<IllegalArgumentException> { Keyword(value) }
    }

    @Test
    fun `검색어에 공백을 입력하면 예외를 발생한다`() {
        // given
        val value = "    "

        // when & then
        assertThrows<IllegalArgumentException> { Keyword(value) }
    }
}
