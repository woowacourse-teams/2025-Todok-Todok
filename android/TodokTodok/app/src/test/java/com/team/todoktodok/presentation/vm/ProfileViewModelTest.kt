package com.team.todoktodok.presentation.vm

import com.team.domain.model.Support
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

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun `전달받은 멤버 아이디가 존재하면 다른 유저의 프로필 화면으로 초기 상태가 설정된다`() =
        runTest {
            // when
            viewModel.initState(10L)

            // then
            val state = viewModel.uiState.getOrAwaitValue()
            state.memberId shouldBe MemberId.OtherUser(id = 10)
            state.isMyProfilePage shouldBe false
        }

    @Test
    fun `전달받은 멤버 아이디가 기본값이면 내 프로필 화면으로 초기 상태가 설정된다`() =
        runTest {
            // when
            viewModel.initState(DEFAULT_MEMBER_ID)

            // then
            val state = viewModel.uiState.getOrAwaitValue()
            state.memberId shouldBe MemberId.Mine
            state.isMyProfilePage shouldBe true
        }

    @Test
    fun `사용자의 프로필을 불러온다`() =
        runTest {
            // given
            val response = Profile(1, "페토", "나나를 좋아하는", "")

            coEvery { repository.getProfile(any()) } returns response

            // when
            viewModel.initState(1)

            // then
            val expected = (viewModel.uiState.getOrAwaitValue().items[1] as ProfileItems.InformationItem).value

            expected shouldBe response
        }

    @Test
    fun `유저를 차단한다`() =
        runTest {
            // given
            val memberId = 2L
            viewModel.initState(memberId)

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
            viewModel.initState(memberId)

            // when
            viewModel.supportMember(Support.REPORT)

            // then
            coVerify { repository.supportMember(MemberId.OtherUser(memberId), Support.REPORT) }
        }
}
