package com.team.todoktodok.data.datasource

import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.core.JwtParser
import com.team.todoktodok.data.datasource.member.DefaultMemberRemoteDataSource
import com.team.todoktodok.data.datasource.token.TokenLocalDataSource
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.ReportRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.LoginResponse
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class DefaultMemberRemoteDataSourceTest {
    private lateinit var memberService: MemberService
    private lateinit var tokenLocalDataSource: TokenLocalDataSource
    private lateinit var dataSource: DefaultMemberRemoteDataSource

    @BeforeEach
    fun setUp() {
        memberService = mockk()
        tokenLocalDataSource = mockk(relaxed = true)
        dataSource = DefaultMemberRemoteDataSource(memberService, tokenLocalDataSource)
    }

    @Test
    fun `로그인시 액세스 토큰을 저장하고 멤버 타입을 반환한다`() =
        runTest {
            val email = "test@example.com"
            val rawAccessToken = "test.jwt.accessToken"
            val rawRefreshToken = "test.jwt.refreshToken"
            val accessToken = "Bearer $rawAccessToken"
            val refreshToken = "Bearer $rawRefreshToken"
            val loginResponse = LoginResponse(refreshToken)
            val memberId = 1L

            val mockResponse = mockk<Response<LoginResponse>>()
            every { mockResponse.isSuccessful } returns true
            every { mockResponse.headers() } returns mapOf("Authorization" to accessToken).toHeaders()
            every { mockResponse.body() } returns loginResponse

            coEvery { memberService.login(LoginRequest(email)) } returns mockResponse

            mockkConstructor(JwtParser::class)
            every { anyConstructed<JwtParser>().parseToMemberType() } returns MemberType.USER
            every { anyConstructed<JwtParser>().parseToMemberId() } returns memberId

            // when
            val result = dataSource.login(email)

            // then
            assertTrue(result is NetworkResult.Success)
            assertEquals(MemberType.USER, (result as NetworkResult.Success).data)
            coVerify { tokenLocalDataSource.saveSetting(accessToken, refreshToken, memberId) }
        }

    @Test
    fun `액세스_토큰을 담은 헤더가 없으면 MissingLocationHeaderException을 반환한다`() =
        runTest {
            val email = "test@example.com"
            val mockResponse = mockk<Response<LoginResponse>>()

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
            val exception = TodokTodokExceptions.UnknownException(null)
            coEvery { tokenLocalDataSource.getMemberId() } returns memberId
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
            coEvery { tokenLocalDataSource.getMemberId() } returns memberId
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
            val response = NetworkResult.Success(mockk<List<DiscussionResponse>>())

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
            val response = NetworkResult.Success(mockk<List<DiscussionResponse>>())

            coEvery { tokenLocalDataSource.getMemberId() } returns memberId
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

            coEvery {
                memberService.report(
                    memberId,
                    ReportRequest(""),
                )
            } returns NetworkResult.Success(Unit)

            // when
            dataSource.supportMember(request, type, "")

            // then
            coVerify(exactly = 1) { memberService.report(memberId, ReportRequest("")) }
            coVerify(exactly = 0) { memberService.block(any()) }
        }

    @Test
    fun `이미 신고한 회원이면 Failure를 반환한다`() =
        runTest {
            // given
            val memberId = 42L
            val request = MemberId.OtherUser(memberId)
            val type = Support.REPORT

            val exception = TodokTodokExceptions.ReportException.AlreadyReportedException

            coEvery {
                memberService.report(
                    memberId,
                    ReportRequest(""),
                )
            } returns NetworkResult.Failure(exception)

            // when
            val result = dataSource.supportMember(request, type, "")

            // then
            assertTrue(result is NetworkResult.Failure)
            assertEquals(exception, (result as NetworkResult.Failure).exception)
            assertEquals("[ERROR] 이미 신고한 회원입니다", exception.message)
        }

    @Test
    fun `신고 타입이 BLOCK이면 차단 API를 호출한다`() =
        runTest {
            // given
            val memberId = 10L
            val request = MemberId.OtherUser(memberId)
            val type = Support.BLOCK

            coEvery { memberService.block(memberId) } returns NetworkResult.Success(Unit)

            // when
            dataSource.supportMember(request, type, "")

            // then
            coVerify(exactly = 0) { memberService.report(any(), ReportRequest("")) }
            coVerify(exactly = 1) { memberService.block(memberId) }
        }

    @Test
    fun `이미 차단한 회원이면 Failure를 반환한다`() =
        runTest {
            // given
            val memberId = 42L
            val request = MemberId.OtherUser(memberId)
            val type = Support.BLOCK

            val exception = TodokTodokExceptions.BlockException.AlreadyBlockedException

            coEvery { memberService.block(memberId) } returns NetworkResult.Failure(exception)

            // when
            val result = dataSource.supportMember(request, type, "")

            // then
            assertTrue(result is NetworkResult.Failure)
            assertEquals(exception, (result as NetworkResult.Failure).exception)
            assertEquals("[ERROR] 이미 차단한 회원입니다", exception.message)
        }

    @Test
    fun `프로필 수정 요청 시 modifyProfile API를 호출한다`() =
        runTest {
            // given
            val request = ModifyProfileRequest("나는", "페토다!")

            coEvery { memberService.modifyProfile(request) } returns NetworkResult.Success(Unit)

            // when
            dataSource.modifyProfile(request)

            // then
            coVerify(exactly = 1) { memberService.modifyProfile(request) }
        }

    @Test
    fun `차단된 멤버 목록을 가져온다`() =
        runTest {
            // given
            val response = NetworkResult.Success(mockk<List<BlockedMemberResponse>>())
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
            coEvery { memberService.unblock(memberId) } returns NetworkResult.Success(Unit)

            // when
            dataSource.unblock(memberId)

            // then
            coVerify(exactly = 1) { memberService.unblock(memberId) }
        }
}
