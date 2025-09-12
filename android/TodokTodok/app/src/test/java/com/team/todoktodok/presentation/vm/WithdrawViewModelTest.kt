package com.team.todoktodok.presentation.vm

import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.view.setting.withdraw.WithdrawUiEvent
import com.team.todoktodok.presentation.view.setting.withdraw.vm.WithdrawViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class WithdrawViewModelTest {
    private val memberRepository: MemberRepository = mockk()

    @Test
    fun `회원탈퇴가 완료되면 NavigateToLogin 이벤트가 발생한다`() =
        runTest {
            // given
            coEvery { memberRepository.withdraw() } returns NetworkResult.Success(Unit)
            val viewModel = WithdrawViewModel(memberRepository)

            // when
            viewModel.withdraw()

            // then
            coVerify(exactly = 1) { memberRepository.withdraw() }
            assertEquals(false, viewModel.isLoading.getOrAwaitValue())
            assertEquals(viewModel.uiEvent.getOrAwaitValue(), (WithdrawUiEvent.NavigateToLogin))
        }
}
