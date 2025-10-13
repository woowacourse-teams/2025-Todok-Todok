package com.team.todoktodok.presentation.vm

import app.cash.turbine.test
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.presentation.xml.auth.login.LoginUiEvent
import com.team.todoktodok.presentation.xml.auth.vm.AuthViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
@ExtendWith(CoroutinesTestExtension::class)
class AuthViewModelTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var tokenRepository: TokenRepository
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setup() {
        memberRepository = mockk()
        tokenRepository = mockk()
        connectivityObserver = mockk()

        every { connectivityObserver.subscribe() } returns emptyFlow()
        coEvery { tokenRepository.getMemberId() } returns 1L

        viewModel =
            AuthViewModel(
                memberRepository,
                tokenRepository,
                connectivityObserver,
            )
    }

    @Test
    fun `memberId가 존재할 경우 메인 화면으로 이동 이벤트가 발생한다`() =
        runTest {
            viewModel.uiEvent.test {
                // when
                viewModel.checkMember()

                // then
                assertEquals(LoginUiEvent.NavigateToMain, awaitItem())

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `memberId가 0이면 로그인 버튼 표시 이벤트가 발생한다`() =
        runTest {
            // given
            coEvery { tokenRepository.getMemberId() } returns 0L

            viewModel.uiEvent.test {
                // when
                viewModel.checkMember()

                // then
                assertEquals(LoginUiEvent.ShowLoginButton, awaitItem())
            }
        }
}
