package com.team.domain.model

import com.team.domain.model.member.MemberType.Companion.MemberType
import com.team.domain.model.member.MemberType.TEMP_USER
import com.team.domain.model.member.MemberType.USER
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MemberTypeTest {
    @Test
    fun `전달받은 role이 USER일 때 MemberType은 USER다`() {
        val role = "USER"
        val memberType = MemberType(role)
        assertEquals(memberType, USER)
    }

    @Test
    fun `전달받은 role이 TEMP_USER일 때 MemberType은 TEMP_USER다`() {
        val role = "TEMP_USER"
        val memberType = MemberType(role)
        assertEquals(memberType, TEMP_USER)
    }
}
