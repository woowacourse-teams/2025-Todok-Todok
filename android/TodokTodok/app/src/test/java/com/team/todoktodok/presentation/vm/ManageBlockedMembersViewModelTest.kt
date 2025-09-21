package com.team.todoktodok.presentation.vm

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.BlockedMember
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.data.core.ext.toLocalDate
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.xml.setting.manage.vm.ManageBlockedMembersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class ManageBlockedMembersViewModelTest {
    private lateinit var repository: MemberRepository
    private lateinit var viewModel: ManageBlockedMembersViewModel

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = ManageBlockedMembersViewModel(repository)
    }

    @Test
    fun `초기화 시 차단된 유저 목록을 불러온다`() =
        runTest {
            // given
            val blockedList = listOf(BlockedMember(1L, "닉네임", "2025-07-30T07:54:24.604Z".toLocalDate()))
            coEvery { repository.getBlockedMembers() } returns NetworkResult.Success(blockedList)

            // when
            viewModel = ManageBlockedMembersViewModel(repository)

            // then
            val actual = viewModel.uiState.getOrAwaitValue().members
            assertEquals(blockedList, actual)
        }

    @Test
    fun `차단된 유저의 차단을 해제하고 차단 목록에서 제거한다`() =
        runTest {
            // given
            val unblockMemberId = 2L
            val members =
                listOf(
                    BlockedMember(1L, "user1", "2025-07-30T07:54:24.604Z".toLocalDate()),
                    BlockedMember(2L, "user2", "2025-07-30T07:54:24.604Z".toLocalDate()),
                )
            coEvery { repository.getBlockedMembers() } returns NetworkResult.Success(members)
            coEvery { repository.unblock(unblockMemberId) } returns NetworkResult.Success(Unit)

            viewModel = ManageBlockedMembersViewModel(repository)

            // when
            viewModel.onSelectMember(unblockMemberId)
            viewModel.unblockMember()

            advanceUntilIdle()

            // then
            val actual = viewModel.uiState.getOrAwaitValue()
            val actualMembers = actual.members

            coVerify { repository.unblock(unblockMemberId) }
            assertEquals(-1, actual.selectedMemberId)
            assertFalse(actualMembers.any { it.memberId == unblockMemberId })
        }
}
