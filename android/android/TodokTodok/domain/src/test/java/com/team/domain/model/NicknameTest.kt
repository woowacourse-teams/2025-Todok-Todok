package com.team.domain.model

import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NicknameTest {
    @Test
    fun `정상적인 닉네임은 예외 없이 생성된다`() {
        assertDoesNotThrow { Nickname("홍길동") }
        assertDoesNotThrow { Nickname("gildong1") }
        assertDoesNotThrow { Nickname("길1a") }
    }

    @Test
    fun `닉네임에 공백이 포함되면 InvalidWhiteSpace 예외 발생한다`() {
        val exception =
            assertThrows<NickNameException.InvalidWhiteSpace> {
                Nickname("홍 길동")
            }
        assertEquals(NickNameException.InvalidWhiteSpace, exception)
    }

    @Test
    fun `닉네임에 특수문자가 포함되면 InvalidCharacters 예외 발생한다`() {
        val exception =
            assertThrows<NickNameException.InvalidCharacters> {
                Nickname("홍길동!")
            }
        assertEquals(NickNameException.InvalidCharacters, exception)
    }

    @Test
    fun `닉네임이 비어있으면 InvalidLength 예외 발생한다`() {
        val exception =
            assertThrows<NickNameException.InvalidLength> {
                Nickname("")
            }
        assertEquals(NickNameException.InvalidLength, exception)
    }

    @Test
    fun `닉네임이 8글자를 초과하면 InvalidLength 예외 발생한다`() {
        val exception =
            assertThrows<NickNameException.InvalidLength> {
                Nickname("가나다라마바사아자차")
            }
        assertEquals(NickNameException.InvalidLength, exception)
    }
}
