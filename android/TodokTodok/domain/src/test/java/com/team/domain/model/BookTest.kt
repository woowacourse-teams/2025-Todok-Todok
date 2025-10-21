package com.team.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BookTest {
    @Test
    fun `책 제목에서 부제를 제거한다`() {
        // given
        val book =
            Book(
                0,
                "오브젝트-코드로 이해하는 객체지향 설계",
                "조영호",
                "",
            )

        // when
        val title = book.extractSubtitle()

        assertEquals("오브젝트", title)
    }

    @Test
    fun `책의 저자중 첫 번째 저자만 남긴다`() {
        // given
        val book =
            Book(
                0,
                "오브젝트-코드로 이해하는 객체지향 설계",
                "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                "",
            )

        // when
        val author = book.extractAuthor()

        assertEquals("Yegor Bugayenko", author)
    }
}
