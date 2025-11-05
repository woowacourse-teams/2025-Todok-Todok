package com.team.data.repository

import com.team.data.datasource.discussion.member.MemberRemoteDataSource
import com.team.data.network.request.ModifyProfileRequest
import com.team.data.network.request.SignUpRequest
import com.team.data.network.response.BlockedMemberResponse
import com.team.data.network.response.ProfileResponse
import com.team.data.network.response.discussion.BookResponse
import com.team.data.network.response.discussion.DiscussionResponse
import com.team.data.network.response.discussion.MemberResponse
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.SignUpException
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.ProfileMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultMemberRepositoryTest {
    private lateinit var memberRemoteDataSource: MemberRemoteDataSource
    private lateinit var defaultMemberRepository: DefaultMemberRepository

    @BeforeEach
    fun setUp() {
        memberRemoteDataSource = mockk()
        defaultMemberRepository = DefaultMemberRepository(memberRemoteDataSource)
    }

    @Test
    fun `로그인 성공 시 MemberType을 반환한다`() =
        runTest {
            // given
            val idToken = "google-id-token"
            val expectedMemberType = MemberType.USER
            coEvery {
                memberRemoteDataSource.login(idToken)
            } returns NetworkResult.Success(expectedMemberType)

            // when
            val result = defaultMemberRepository.login(idToken)

            // then
            assertTrue(result is NetworkResult.Success)
            assertEquals(expectedMemberType, (result as NetworkResult.Success).data)
        }

    @Test
    fun `회원가입 시 캐시된 토큰을 사용한다`() =
        runTest {
            // given
            val idToken = "google-id-token"
            val nickname = Nickname("테스트유저")
            coEvery { memberRemoteDataSource.login(idToken) } returns NetworkResult.Success(MemberType.TEMP_USER)
            coEvery {
                memberRemoteDataSource.signUp(SignUpRequest(nickname.value, idToken))
            } returns NetworkResult.Success(Unit)

            // 먼저 로그인하여 토큰 캐시
            defaultMemberRepository.login(idToken)

            // when
            val result = defaultMemberRepository.signUp(nickname)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { memberRemoteDataSource.signUp(SignUpRequest(nickname.value, idToken)) }
        }

    @Test
    fun `캐시된 토큰이 없으면 회원가입 시 InvalidToken 예외가 발생한다`() =
        runTest {
            // given
            val nickname = Nickname("테스트유저")

            // when
            val result = defaultMemberRepository.signUp(nickname)

            // then
            assertTrue(result is NetworkResult.Failure)
            val exception = (result as NetworkResult.Failure).exception
            assertTrue(exception is SignUpException.InvalidToken)
        }

    @Test
    fun `프로필 조회 시 도메인 모델로 변환된다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser(123L)
            val profileResponse =
                ProfileResponse(
                    memberId = 123L,
                    nickname = "테스트유저",
                    profileImage = "https://example.com/profile.jpg",
                    profileMessage = "안녕하세요",
                )
            coEvery {
                memberRemoteDataSource.fetchProfile(memberId)
            } returns NetworkResult.Success(profileResponse)

            // when
            val result = defaultMemberRepository.getProfile(memberId)

            // then
            assertTrue(result is NetworkResult.Success)
            val profile = (result as NetworkResult.Success).data
            assertEquals("테스트유저", profile.nickname)
            assertEquals("안녕하세요", profile.message)
        }

    @Test
    fun `멤버 토론방 조회 시 도메인 모델로 변환된다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser(123L)
            val discussionType = MemberDiscussionType.CREATED
            val discussionResponses =
                listOf(
                    DiscussionResponse(
                        discussionId = 1L,
                        discussionTitle = "토론 제목",
                        discussionOpinion = "찬성",
                        commentCount = 5,
                        likeCount = 10,
                        viewCount = 100,
                        createdAt = "2024-01-01T00:00:00",
                        book =
                            BookResponse(
                                bookId = 1L,
                                bookTitle = "책 제목",
                                bookAuthor = "저자",
                                bookImage = "https://example.com/book.jpg",
                            ),
                        member =
                            MemberResponse(
                                memberId = 123L,
                                nickname = "테스트유저",
                                profileImage = "https://example.com/profile.jpg",
                            ),
                        isLikedByMe = false,
                    ),
                )
            coEvery {
                memberRemoteDataSource.fetchMemberDiscussionRooms(memberId, discussionType)
            } returns NetworkResult.Success(discussionResponses)

            // when
            val result = defaultMemberRepository.getMemberDiscussionRooms(memberId, discussionType)

            // then
            assertTrue(result is NetworkResult.Success)
            val discussions = (result as NetworkResult.Success).data
            assertEquals(1, discussions.size)
            assertEquals("토론 제목", discussions[0].discussionTitle)
        }

    @Test
    fun `멤버 지원(차단_신고) 요청을 전달한다`() =
        runTest {
            // given
            val memberId = MemberId.OtherUser(123L)
            val supportType = Support.BLOCK
            val reason = "스팸"
            coEvery {
                memberRemoteDataSource.supportMember(memberId, supportType, reason)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultMemberRepository.supportMember(memberId, supportType, reason)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { memberRemoteDataSource.supportMember(memberId, supportType, reason) }
        }

    @Test
    fun `프로필 수정 요청을 전달한다`() =
        runTest {
            // given
            val nickname = Nickname("새로운닉네임")
            val message = ProfileMessage("새로운 메시지")
            coEvery {
                memberRemoteDataSource.modifyProfile(ModifyProfileRequest(nickname.value, message.value))
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultMemberRepository.modifyProfile(nickname, message)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify {
                memberRemoteDataSource.modifyProfile(ModifyProfileRequest(nickname.value, message.value))
            }
        }

    @Test
    fun `차단된 멤버 목록 조회 시 도메인 모델로 변환된다`() =
        runTest {
            // given
            val blockedMembers =
                listOf(
                    BlockedMemberResponse(
                        memberId = 1L,
                        nickname = "차단유저1",
                        createdAt = "2024-01-01T00:00:00",
                    ),
                    BlockedMemberResponse(
                        memberId = 2L,
                        nickname = "차단유저2",
                        createdAt = "2024-01-02T00:00:00",
                    ),
                )
            coEvery {
                memberRemoteDataSource.fetchBlockedMembers()
            } returns NetworkResult.Success(blockedMembers)

            // when
            val result = defaultMemberRepository.getBlockedMembers()

            // then
            assertTrue(result is NetworkResult.Success)
            val members = (result as NetworkResult.Success).data
            assertEquals(2, members.size)
            assertEquals("차단유저1", members[0].nickname)
        }

    @Test
    fun `차단 해제 요청을 전달한다`() =
        runTest {
            // given
            val memberId = 123L
            coEvery { memberRemoteDataSource.unblock(memberId) } returns NetworkResult.Success(Unit)

            // when
            val result = defaultMemberRepository.unblock(memberId)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { memberRemoteDataSource.unblock(memberId) }
        }

    @Test
    fun `회원 탈퇴 요청을 전달한다`() =
        runTest {
            // given
            coEvery { memberRemoteDataSource.withdraw() } returns NetworkResult.Success(Unit)

            // when
            val result = defaultMemberRepository.withdraw()

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { memberRemoteDataSource.withdraw() }
        }
}
