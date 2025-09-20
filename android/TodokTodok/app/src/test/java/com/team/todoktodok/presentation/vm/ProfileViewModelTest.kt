package com.team.todoktodok.presentation.vm

import android.content.ContentResolver
import android.net.Uri
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems
import com.team.todoktodok.presentation.view.profile.vm.ProfileViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class ProfileViewModelTest {
    private lateinit var repository: MemberRepository
    private lateinit var tokenRepository: TokenRepository
    private lateinit var viewModel: ProfileViewModel

    private fun setMockProfileData() {
        coEvery { repository.getProfile(any()) } returns
            NetworkResult.Success(Profile(1, "페토", "안녕하세요", ""))
        coEvery { repository.modifyProfileImage(any()) } returns
            NetworkResult.Success("newImage")
        coEvery { repository.getMemberBooks(any()) } returns NetworkResult.Success(emptyList())
        coEvery { repository.getMemberDiscussionRooms(any(), any()) } returns
            NetworkResult.Success(
                emptyList(),
            )
        coEvery { tokenRepository.getMemberId() } returns 1
    }

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        tokenRepository = mockk(relaxed = true)
        viewModel = ProfileViewModel(repository, tokenRepository)
    }

    @Test
    fun `전달받은 멤버 아이디가 내 아이디와 다르다면 다른 유저의 프로필 화면으로 초기 상태가 설정된다`() =
        runTest {
            // given
            val id = 10L
            setMockProfileData()

            // when
            viewModel.loadProfile(id)

            // then
            val state = viewModel.uiState.getOrAwaitValue()
            state.memberId shouldBe MemberId.OtherUser(id = 10)
            state.isMyProfilePage shouldBe false
        }

    @Test
    fun `전달받은 멤버 아이디가 기본값이면 내 프로필 화면으로 초기 상태가 설정된다`() =
        runTest {
            // given
            setMockProfileData()

            // when
            viewModel.loadProfile(1)

            // then
            val state = viewModel.uiState.getOrAwaitValue()
            state.memberId shouldBe MemberId.Mine
            state.isMyProfilePage shouldBe true
        }

    @Test
    fun `사용자의 프로필을 불러온다`() =
        runTest {
            // given
            val profileResponse = Profile(1, "페토", "나나를 좋아하는", "")
            coEvery { repository.getProfile(any()) } returns NetworkResult.Success(profileResponse)
            coEvery { repository.getMemberBooks(any()) } returns NetworkResult.Success(emptyList())
            coEvery {
                repository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(emptyList())

            // when
            viewModel.loadProfile(1)

            // then
            val expected =
                (viewModel.uiState.getOrAwaitValue().items[1] as ProfileItems.InformationItem).value
            expected shouldBe profileResponse
        }

    @Test
    fun `유저를 차단한다`() =
        runTest {
            // given
            val memberId = 2L
            setMockProfileData()
            viewModel.loadProfile(memberId)

            // when
            viewModel.supportMember(Support.BLOCK, "")

            // then
            coVerify { repository.supportMember(MemberId.OtherUser(memberId), Support.BLOCK, "") }
        }

    @Test
    fun `유저를 신고한다`() =
        runTest {
            // given
            val memberId = 2L
            viewModel.loadProfile(memberId)
            setMockProfileData()

            // when
            viewModel.supportMember(Support.REPORT, "")

            // then
            coVerify { repository.supportMember(MemberId.OtherUser(memberId), Support.REPORT, "") }
        }

    @Test
    fun `프로필을 새로고침한다`() =
        runTest {
            // given
            val initialProfile = Profile(1, "페토", "하나코", "")
            val updatedProfile = Profile(1, "정페토", "나나", "")

            coEvery { repository.getProfile(any()) } returns NetworkResult.Success(initialProfile)
            coEvery { repository.getMemberBooks(any()) } returns NetworkResult.Success(emptyList())
            coEvery {
                repository.getMemberDiscussionRooms(
                    any(),
                    any(),
                )
            } returns NetworkResult.Success(emptyList())

            viewModel.loadProfile(1)

            coEvery { repository.getProfile(any()) } returns NetworkResult.Success(updatedProfile)

            // when
            viewModel.refreshProfile()

            // then
            val state = viewModel.uiState.getOrAwaitValue()
            val infoItem =
                state.items
                    .first { it is ProfileItems.InformationItem } as ProfileItems.InformationItem
            infoItem.value shouldBe updatedProfile
        }

    @Test
    fun `initState 호출 시 4개의 데이터 로드 메서드가 호출된다`() =
        runTest {
            // given
            setMockProfileData()

            // when
            viewModel.loadProfile(5)

            // then
            coVerify(exactly = 1) { repository.getProfile(any()) }
            coVerify(exactly = 1) { repository.getMemberBooks(any()) }
            coVerify(exactly = 1) {
                repository.getMemberDiscussionRooms(
                    any(),
                    MemberDiscussionType.PARTICIPATED,
                )
            }
            coVerify(exactly = 1) {
                repository.getMemberDiscussionRooms(
                    any(),
                    MemberDiscussionType.CREATED,
                )
            }
        }

    @Test
    fun `프로필 이미지 업데이트 성공 시 uiState의 profileImage가 갱신된다`() =
        runTest {
            // given
            setMockProfileData()
            viewModel.loadProfile(1)
            advanceUntilIdle()

            val uri = mockk<Uri>(relaxed = true)
            val contentResolver = mockk<ContentResolver>(relaxed = true)

            // when
            viewModel.updateProfile(imageUri = uri, contentResolver = contentResolver)
            advanceUntilIdle()

            // then
            val info =
                viewModel.uiState
                    .getOrAwaitValue()
                    .items[1] as ProfileItems.InformationItem
            info.value.profileImage shouldBe "newImage"
        }
}
