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
    fun `signUp 호출 시 remote 데이터소스를 통해 회원가입하고 Member 도메인을 반환한다`() =
        runTest {
            // given
            val requestEmail = "test@email.com"
            fakeDataSource.expectedRequest = requestEmail
            fakeDataSource.response = SignUpResponse("페토", "", requestEmail)

            // when
            val result = repository.signUp(requestEmail)

            // then
            assertEquals(Member("페토", "", requestEmail), result)
        }
}
