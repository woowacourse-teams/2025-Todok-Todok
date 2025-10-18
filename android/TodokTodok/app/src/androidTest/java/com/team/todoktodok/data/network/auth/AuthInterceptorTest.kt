package com.team.todoktodok.data.network.auth

import com.team.todoktodok.data.datasource.FakeTokenLocalDataSource
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthInterceptorTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        client =
            OkHttpClient
                .Builder()
                .addInterceptor(AuthInterceptor(FakeTokenLocalDataSource()))
                .build()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.close()
    }

    @Test
    fun `네트워크_요청_헤더에_엑세스_토큰을_추가한다`() {
        // given
        mockWebServer.enqueue(MockResponse().setBody("OK"))

        val request =
            Request
                .Builder()
                .url(mockWebServer.url("/test"))
                .build()

        // when
        client.newCall(request).execute().use { response ->
            // then
            val recordedRequest = mockWebServer.takeRequest()
            val header = recordedRequest.getHeader("Authorization")

            assertEquals("Bearer fake_access_token", header)
            assertEquals(200, response.code)
        }
    }
}
