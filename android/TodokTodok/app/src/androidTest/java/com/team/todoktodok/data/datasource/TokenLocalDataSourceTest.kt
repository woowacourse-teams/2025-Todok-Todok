package com.team.todoktodok.data.datasource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TokenLocalDataSourceTest {
    private lateinit var tokenLocalDataSource: TokenLocalDataSource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tokenLocalDataSource = TokenLocalDataSource(context)
    }

    @Test
    fun `accessToken을 저장 후 가져온다`() =
        runTest {
            tokenLocalDataSource.saveAccessToken("testAccess")
            val result = tokenLocalDataSource.getAccessToken()
            assertEquals("testAccess", result)
        }

    @Test
    fun `refreshToken을 저장 후 가져온다`() =
        runTest {
            tokenLocalDataSource.saveToken("access123", "refresh123")
            val result = tokenLocalDataSource.getRefreshToken()
            assertEquals("refresh123", result)
        }

    @Test
    fun `memberId을 저장 후 가져온다`() =
        runTest {
            tokenLocalDataSource.saveSetting("a", "r", 123L)
            val result = tokenLocalDataSource.getMemberId()
            assertEquals(123L, result)
        }

    @Test
    fun `clear 호출 시 기본값으로 초기화된다`() =
        runTest {
            tokenLocalDataSource.saveSetting("a", "r", 123L)
            tokenLocalDataSource.clear()

            val accessToken = tokenLocalDataSource.getAccessToken()
            val refreshToken = tokenLocalDataSource.getRefreshToken()
            val memberId = tokenLocalDataSource.getMemberId()

            assertEquals("", accessToken)
            assertEquals("", refreshToken)
            assertEquals(0L, memberId)
        }
}
