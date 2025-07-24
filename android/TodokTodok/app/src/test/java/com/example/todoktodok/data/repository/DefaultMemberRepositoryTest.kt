package com.example.todoktodok.data.repository

import com.example.domain.model.Member
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.data.network.response.SignUpResponse
import com.example.todoktodok.fake.datasource.FakeMemberDataSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultMemberRepositoryTest {
    private lateinit var fakeDataSource: FakeMemberDataSource
    private lateinit var repository: MemberRepository

    @BeforeEach
    fun setUp() {
        fakeDataSource = FakeMemberDataSource()
        repository = DefaultMemberRepository(fakeDataSource)
    }

    @Test
    fun `회원가입에 성공하면 회원 정보 응답값을 반환받는다`() =
        runTest {
            // given
            val member = Member(email = "test@example.com", nickName = "테스트", profileImage = "")
            val expectedResponse =
                SignUpResponse(email = "test@example.com", nickName = "테스트", profileImage = "")
            fakeDataSource.response = expectedResponse

            // when
            val result = repository.signUp(member)

            // then
            assertEquals(member.email, result.email)
            assertEquals(member.nickName, result.nickName)
        }
}
