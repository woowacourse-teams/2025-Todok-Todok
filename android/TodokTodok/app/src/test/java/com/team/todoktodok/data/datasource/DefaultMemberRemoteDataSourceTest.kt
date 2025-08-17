package com.team.todoktodok.data.datasource

import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.core.JwtParser
import com.team.todoktodok.data.datasource.member.DefaultMemberRemoteDataSource
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.MemberDiscussionResponse
import com.team.todoktodok.data.network.service.MemberService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.test.runTest
import okhttp3.Headers.Companion.toHeaders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
            val accessToken = "Bearer $rawToken"
            val memberId = 1L

            val mockResponse = mockk<Response<Unit>>()
            every { mockResponse.isSuccessful } returns true
            every { mockResponse.headers() } returns mapOf(AUTHORIZATION_NAME to accessToken).toHeaders()

            coEvery { memberService.login(LoginRequest(email)) } returns mockResponse

            mockkConstructor(JwtParser::class)
            every { anyConstructed<JwtParser>().parseToMemberType() } returns MemberType.USER
            every { anyConstructed<JwtParser>().parseToMemberId() } returns memberId

            // when
            val result = dataSource.login(email)

            // then
            assertTrue(result is NetworkResult.Success)
            assertEquals(MemberType.USER, (result as NetworkResult.Success).data)
            coVerify { tokenDataSource.saveToken(accessToken, "", memberId) }
        }

    @Test
    fun `로그인 실패 시 토큰을 담은 헤더가 없으면 MissingLocationHeaderException을 반환한다`() =
        runTest {
            val email = "test@example.com"
            val mockResponse = mockk<Response<Unit>>()

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.headers() } returns emptyMap<String, String>().toHeaders()

            coEvery { memberService.login(LoginRequest(email)) } returns mockResponse

            val result = dataSource.login(email)

            // then
            assertTrue(result is NetworkResult.Failure)

            val failure = result as NetworkResult.Failure
            assertTrue(failure.exception is TodokTodokExceptions.MissingLocationHeaderException)
        }

    @Test
    fun `유저 정보 API를 호출할 때 MemberId를 전달 받았으면 전달받은 memberID를 사용해 API를 호출한다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser(1L)
            val profileResponse = mockk<ProfileResponse>()

            coEvery { memberService.fetchProfile(memberId.id) } returns
                NetworkResult.Success(
                    profileResponse,
                )

            // when
            val result = dataSource.fetchProfile(memberId)

            // then
            assertEquals(NetworkResult.Success(profileResponse), result)
        }

    @Test
    fun `유저 정보 API 호출 시 실패하면 Failure를 반환한다`() =
        runTest {
            // given
            val memberId = 2L
            val exception = TokdokTodokExceptions.UnknownException
            coEvery { tokenDataSource.getMemberId() } returns memberId
            coEvery { memberService.fetchProfile(memberId) } returns NetworkResult.Failure(exception)

            // when
            val result = dataSource.fetchProfile(MemberId.Mine)

            // then
            assertEquals(NetworkResult.Failure(exception), result)
        }

    @Test
    fun `유저 정보 API를 호출할 때 MemberId가 없다면 TokenDataSource를 호출해 memberID를 받아와 API를 호출한다`() =
        runTest {
            // given
            val memberId = 2L
            val profileResponse = mockk<ProfileResponse>()
            coEvery { tokenDataSource.getMemberId() } returns memberId
            coEvery { memberService.fetchProfile(memberId) } returns
                NetworkResult.Success(
                    profileResponse,
                )

            // when
            val result = dataSource.fetchProfile(MemberId.Mine)

            // then
            assertEquals(NetworkResult.Success(profileResponse), result)
        }

    @Test
    fun `토론방 목록 조회시 memberId가 주어지면 해당 memberId로 요청한다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser(1L)
            val type = MemberDiscussionType.CREATED.name
            val response = mockk<List<MemberDiscussionResponse>>()

            coEvery { memberService.fetchMemberDiscussionRooms(memberId.id, type) } returns response

            // when
            val result =
                dataSource.fetchMemberDiscussionRooms(memberId, MemberDiscussionType.CREATED)

            // then
            assertEquals(response, result)
        }

    @Test
    fun `토론방 목록 조회시 memberId가 null이면 TokenDataSource에서 가져온다`() =
        runTest {
            // given
            val memberId = 1L
            val type = MemberDiscussionType.PARTICIPATED
            val response = mockk<List<MemberDiscussionResponse>>()

            coEvery { tokenDataSource.getMemberId() } returns memberId
            coEvery {
                memberService.fetchMemberDiscussionRooms(
                    memberId,
                    type.name,
                )
            } returns response

            // when
            val result = dataSource.fetchMemberDiscussionRooms(MemberId.Mine, type)

            // then
            assertEquals(response, result)
        }

    @Test
    fun `신고 타입이 Report면 신고 API를 호출한다`() =
        runTest {
            // given
            val memberId = 10L
            val request = MemberId.OtherUser(memberId)
            val type = Support.REPORT

            coEvery { memberService.report(memberId) } just Runs

            // when
            dataSource.supportMember(request, type)

            // then
            coVerify(exactly = 1) { memberService.report(memberId) }
            coVerify(exactly = 0) { memberService.block(any()) }
        }

    @Test
    fun `신고 타입이 BLOCK이면 차단 API를 호출한다`() =
        runTest {
            // given
            val memberId = 10L
            val request = MemberId.OtherUser(memberId)
            val type = Support.BLOCK

            coEvery { memberService.block(memberId) } just Runs

            // when
            dataSource.supportMember(request, type)

            // then
            coVerify(exactly = 0) { memberService.report(any()) }
            coVerify(exactly = 1) { memberService.block(memberId) }
        }

    @Test
    fun `프로필 수정 요청 시 modifyProfile API를 호출한다`() =
        runTest {
            // given
            val request = ModifyProfileRequest("나는", "페토다!")

            coEvery { memberService.modifyProfile(request) } just Runs

            // when
            dataSource.modifyProfile(request)

            // then
            coVerify(exactly = 1) { memberService.modifyProfile(request) }
        }

    @Test
    fun `차단된 멤버 목록을 가져온다`() =
        runTest {
            // given
            val response = mockk<List<BlockedMemberResponse>>()
            coEvery { memberService.fetchBlockedMembers() } returns response

            // when
            val result = dataSource.fetchBlockedMembers()

            // then
            assertEquals(response, result)
        }

    @Test
    fun `차단 해제 요청 시 unblock API를 호출한다`() =
        runTest {
            // given
            val memberId = 123L
            coEvery { memberService.unblock(memberId) } just Runs

            // when
            dataSource.unblock(memberId)

            // then
            coVerify(exactly = 1) { memberService.unblock(memberId) }
        }
}
