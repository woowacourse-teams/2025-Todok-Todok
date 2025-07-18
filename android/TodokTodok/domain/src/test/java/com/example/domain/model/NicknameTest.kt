package com.example.domain.model

import com.example.domain.model.member.NickNameException
import com.example.domain.model.member.Nickname
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class NicknameTest {
    @ParameterizedTest
    @ValueSource(strings = [" ", "  ", " 페토", " 동전 ", "모 찌"])
    fun `닉네임은 공백 금지다`(value: String) {
        assertThatThrownBy { Nickname(value) }.isInstanceOf(NickNameException.InvalidWhiteSpace::class.java)
    }
}
