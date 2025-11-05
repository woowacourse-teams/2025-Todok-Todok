package com.team.data.repository

import com.team.data.datasource.token.TokenLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultTokenRepositoryTest {
    private lateinit var tokenLocalDataSource: TokenLocalDataSource
    private lateinit var defaultTokenRepository: DefaultTokenRepository

    @BeforeEach
    fun setUp() {
        tokenLocalDataSource = mockk()
        defaultTokenRepository = DefaultTokenRepository(tokenLocalDataSource)
    }

    @Test
    fun `멤버 ID를 로컬 데이터소스에서 가져온다`() =
        runTest {
            // given
            val expectedMemberId = 123L
            coEvery { tokenLocalDataSource.getMemberId() } returns expectedMemberId

            // when
            val result = defaultTokenRepository.getMemberId()

            // then
            assertEquals(expectedMemberId, result)
            coVerify { tokenLocalDataSource.getMemberId() }
        }

    @Test
    fun `로그아웃 시 로컬 데이터소스를 클리어한다`() =
        runTest {
            // given
            coEvery { tokenLocalDataSource.clear() } returns Unit

            // when
            defaultTokenRepository.logout()

            // then
            coVerify { tokenLocalDataSource.clear() }
        }
}
