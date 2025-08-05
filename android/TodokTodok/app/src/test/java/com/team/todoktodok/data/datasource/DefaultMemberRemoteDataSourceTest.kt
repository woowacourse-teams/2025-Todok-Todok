package com.team.todoktodok.data.datasource

import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.todoktodok.data.core.JwtParser
import com.team.todoktodok.data.datasource.member.DefaultMemberRemoteDataSource
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.service.MemberService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.test.runTest
import okhttp3.Headers.Companion.toHeaders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class DefaultMemberRemoteDataSourceTest {
    private lateinit var memberService: MemberService
    private lateinit var tokenDataSource: TokenDataSource
    private lateinit var dataSource: DefaultMemberRemoteDataSource

    @BeforeEach
    fun setUp() {
        memberService = mockk()
        tokenDataSource = mockk(relaxed = true)
        dataSource = DefaultMemberRemoteDataSource(memberService, tokenDataSource)
    }

    @Test
    fun `로그인시 액세스 토큰을 저장하고 멤버 타입을 반환한다`() =
        runTest {
            val email = "test@example.com"
            val rawToken = "test.jwt.token"
            val accessTokenWithPrefix = "Bearer $rawToken"
            val memberType = "NORMAL"
            val memberId = "123"

            val mockResponse = mockk<Response<Unit>>()

            val headers = mapOf(AUTHORIZATION_NAME to accessTokenWithPrefix)

            val responseHeaders = headers.toHeaders()
            every { mockResponse.headers() } returns responseHeaders

            coEvery { memberService.login(LoginRequest(email)) } returns mockResponse

            mockkConstructor(JwtParser::class)
            every { anyConstructed<JwtParser>().parseMemberType() } returns memberType
            every { anyConstructed<JwtParser>().parseMemberId() } returns memberId

            // when
            val result = dataSource.login(email)

            // then
            assertEquals(memberType, result)
            coVerify { tokenDataSource.saveToken(accessTokenWithPrefix, "", memberId) }
        }

    @Test
    fun `유저 정보 API를 호출할 때 MemberId를 전달 받았으면 전달받은 memberID를 사용해 API를 호출한다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser("1")
            val profileResponse = mockk<ProfileResponse>()

            coEvery { memberService.fetchProfile(memberId.id) } returns profileResponse

            // when
            val result = dataSource.fetchProfile(memberId)

            // then
            assertEquals(profileResponse, result)
        }

    @Test
    fun `유저 정보 API를 호출할 때 MemberId가 없다면 TokenDataSource를 호출해 memberID를 받아와 API를 호출한다`() =
        runTest {
            // given
            val memberId = "2"
            val profileResponse = mockk<ProfileResponse>()
            coEvery { tokenDataSource.getMemberId() } returns memberId
            coEvery { memberService.fetchProfile(memberId) } returns profileResponse

            // when
            val result = dataSource.fetchProfile(MemberId.Mine)

            // then
            assertEquals(profileResponse, result)
        }

    @Test
    fun `토론방 목록 조회시 memberId가 주어지면 해당 memberId로 요청한다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser("5")
            val type = MemberDiscussionType.CREATED.name
            val response = mockk<List<DiscussionResponse>>()

            coEvery { memberService.fetchMemberDiscussionRooms(memberId.id, type) } returns response

            // when
            val result = dataSource.fetchMemberDiscussionRooms(memberId, MemberDiscussionType.CREATED)

            // then
            assertEquals(response, result)
        }

    @Test
    fun `토론방 목록 조회시 memberId가 null이면 TokenDataSource에서 가져온다`() =
        runTest {
            // given
            val memberId = "10"
            val type = MemberDiscussionType.PARTICIPATED
            val response = mockk<List<DiscussionResponse>>()

            coEvery { tokenDataSource.getMemberId() } returns memberId
            coEvery { memberService.fetchMemberDiscussionRooms(memberId, type.name) } returns response

            // when
            val result = dataSource.fetchMemberDiscussionRooms(MemberId.Mine, type)

            // then
            assertEquals(response, result)
        }
}
