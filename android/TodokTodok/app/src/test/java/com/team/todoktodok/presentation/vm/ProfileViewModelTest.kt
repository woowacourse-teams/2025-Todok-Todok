package com.team.todoktodok.presentation.vm

import com.team.domain.model.Support
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberId.Companion.DEFAULT_MEMBER_ID
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class ProfileViewModelTest {
    private lateinit var repository: MemberRepository
    private lateinit var viewModel: ProfileViewModel

    private fun setMockProfileData() {
        coEvery { repository.getProfile(any()) } returns
            Profile(1, "페토", "안녕하세요", "")
        coEvery { repository.getMemberBooks(any()) } returns emptyList()
        coEvery { repository.getMemberDiscussionRooms(any(), any()) } returns emptyList()
    }

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun `전달받은 멤버 아이디가 존재하면 다른 유저의 프로필 화면으로 초기 상태가 설정된다`() =
        runTest {
            // given
            val id = 10L
            viewModel.setMemberId(id)
            setMockProfileData()

            // when
            viewModel.initState()

            // then
            val state = viewModel.uiState.getOrAwaitValue()
            state.memberId shouldBe MemberId.OtherUser(id = 10)
            state.isMyProfilePage shouldBe false
        }

    @Test
    fun `전달받은 멤버 아이디가 기본값이면 내 프로필 화면으로 초기 상태가 설정된다`() =
        runTest {
            // given
            viewModel.setMemberId(DEFAULT_MEMBER_ID)
            setMockProfileData()

            // when
            viewModel.initState()

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
            coEvery { repository.getProfile(any()) } returns profileResponse
            coEvery { repository.getMemberBooks(any()) } returns emptyList()
            coEvery { repository.getMemberDiscussionRooms(any(), any()) } returns emptyList()

            viewModel.setMemberId(1)

            // when
            viewModel.initState()

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
            viewModel.setMemberId(memberId)
            setMockProfileData()
            viewModel.initState()

            // when
            viewModel.supportMember(Support.BLOCK)

            // then
            coVerify { repository.supportMember(MemberId.OtherUser(memberId), Support.BLOCK) }
        }

    @Test
    fun `유저를 신고한다`() =
        runTest {
            // given
            val memberId = 2L
            viewModel.setMemberId(memberId)
            setMockProfileData()
            viewModel.initState()

            // when
            viewModel.supportMember(Support.REPORT)

            // then
            coVerify { repository.supportMember(MemberId.OtherUser(memberId), Support.REPORT) }
        }

    @Test
    fun `프로필을 새로고침한다`() =
        runTest {
            // given
            val initialProfile = Profile(1, "페토", "하나코", "")
            val updatedProfile = Profile(1, "정페토", "나나", "")

            coEvery { repository.getProfile(any()) } returns initialProfile
            coEvery { repository.getMemberBooks(any()) } returns emptyList()
            coEvery { repository.getMemberDiscussionRooms(any(), any()) } returns emptyList()

            viewModel.setMemberId(1)
            viewModel.initState()

            coEvery { repository.getProfile(any()) } returns updatedProfile

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
            viewModel.setMemberId(5)
            setMockProfileData()

            // when
            viewModel.initState()

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
}
