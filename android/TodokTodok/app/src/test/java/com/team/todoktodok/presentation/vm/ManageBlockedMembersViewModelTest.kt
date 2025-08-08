package com.team.todoktodok.presentation.vm

import com.team.domain.model.member.BlockedMember
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.data.core.ext.toLocalDate
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.setting.manage.vm.ManageBlockedMembersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
            coEvery { repository.getBlockedMembers() } returns blockedList

            // when
            viewModel = ManageBlockedMembersViewModel(repository)

            // then
            assertEquals(blockedList, viewModel.blockedMembers.getOrAwaitValue())
        }

    @Test
    fun `차단된 유저의 차단을 해제한다`() =
        runTest {
            // given
            val members =
                listOf(
                    BlockedMember(1L, "user1", "2025-07-30T07:54:24.604Z".toLocalDate()),
                    BlockedMember(2L, "user2", "2025-07-30T07:54:24.604Z".toLocalDate()),
                )
            coEvery { repository.getBlockedMembers() } returns members
            viewModel = ManageBlockedMembersViewModel(repository)

            // when
            viewModel.findMember(1)
            viewModel.unblockMember()

            // then
            coVerify { repository.unblock(2L) }
            assertEquals(1, viewModel.blockedMembers.getOrAwaitValue().size)
            assertFalse(viewModel.blockedMembers.value!!.any { it.memberId == 2L })
        }
}
