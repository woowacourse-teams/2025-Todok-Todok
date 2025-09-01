package com.team.todoktodok.data.datasource

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.datasource.book.DefaultBookRemoteDataSource
import com.team.todoktodok.data.network.adapter.TodokTodokCallAdapterFactory
import com.team.todoktodok.data.network.service.BookService
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit

class DefaultBookRemoteDataSourceTest {
    private lateinit var server: MockWebServer
    private lateinit var retrofit: Retrofit
    private lateinit var service: BookService
    private lateinit var dataSource: DefaultBookRemoteDataSource

    @BeforeEach
    fun setup() {
        server = MockWebServer().also { it.start() }
        retrofit =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .client(OkHttpClient())
                .addCallAdapterFactory(TodokTodokCallAdapterFactory())
                .addConverterFactory(
                    Json {
                        ignoreUnknownKeys = true
                    }.asConverterFactory("application/json".toMediaType()),
                ).build()
        service = retrofit.create(BookService::class.java)
        dataSource = DefaultBookRemoteDataSource(service)
    }

    @AfterEach
    fun clear() {
        server.shutdown()
    }

    @Test
    fun `요청 경로와 쿼리가 정확하다`() =
        runTest {
            server.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody("[]")
                    .addHeader("Content-Type", "application/json"),
            )

            dataSource.fetchBooks("오브젝트")

            val requestUrl = server.takeRequest()

            assertTrue(requestUrl.path!!.startsWith("/v1/books/search"))
            assertEquals(requestUrl.requestUrl!!.queryParameter("keyword"), "오브젝트")
        }

    @Test
    fun `200이면 Success로 파싱되고, keyword가 제대로 붙는다`() =
        runTest {
            server.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        """
                         [
                            {
                                "bookId": "9791158391409",
                                "bookTitle": "오브젝트 - 코드로 이해하는 객체지향 설계",
                                "bookAuthor": "조영호 (지은이)",
                                "bookImage": "https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg"
                            }
                        ]
                        """,
                    ).addHeader("Content-Type", "application/json"),
            )

            val result = dataSource.fetchBooks("오브젝트")

            val request = server.takeRequest()

            val keyword = request.requestUrl!!.queryParameter("keyword")

            assertAll(
                { assertEquals("오브젝트", keyword) },
                { assertTrue(result is NetworkResult.Success) },
                { assertEquals(1, (result as NetworkResult.Success).data.size) },
            )
        }

    @Test
    fun `400이면 Failure로 파싱된다`() =
        runTest {
            server.enqueue(MockResponse().setResponseCode(400))

            val result = dataSource.fetchBooks("오브젝트")

            assertTrue(result is NetworkResult.Failure)
        }

    @Test
    fun `401이면 Failure로 파싱된다`() =
        runTest {
            server.enqueue(MockResponse().setResponseCode(401))

            val result = dataSource.fetchBooks("오브젝트")

            assertTrue(result is NetworkResult.Failure)
        }

    @Test
    fun `500이면 Failure로 파싱된다`() =
        runTest {
            server.enqueue(MockResponse().setResponseCode(500))

            val result = dataSource.fetchBooks("오브젝트")

            assertTrue(result is NetworkResult.Failure)
        }
}
