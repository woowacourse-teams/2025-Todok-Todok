package com.team.ui_xml.auth.vm

import app.cash.turbine.test
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository
import com.team.ui_xml.CoroutinesTestExtension
import com.team.ui_xml.InstantTaskExecutorExtension
import com.team.ui_xml.auth.login.LoginUiEvent
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

        every { connectivityObserver.subscribe(any()) } returns emptyFlow()
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

    @Test
    fun `login 성공 시 USER 타입이면 메인 화면으로 이동한다`() =
        runTest {
            // given
            val idToken = "test-id-token"
            every { connectivityObserver.value() } returns ConnectivityObserver.Status.Available
            coEvery { memberRepository.login(idToken) } returns
                com.team.domain.model.exception.NetworkResult.Success(
                    com.team.domain.model.member.MemberType.USER
                )

            viewModel.uiEvent.test {
                // when
                viewModel.login(idToken)

                // then
                assertEquals(LoginUiEvent.NavigateToMain, awaitItem())
            }
        }

    @Test
    fun `login 성공 시 TEMP_USER 타입이면 회원가입 화면으로 이동한다`() =
        runTest {
            // given
            val idToken = "test-id-token"
            every { connectivityObserver.value() } returns ConnectivityObserver.Status.Available
            coEvery { memberRepository.login(idToken) } returns
                com.team.domain.model.exception.NetworkResult.Success(
                    com.team.domain.model.member.MemberType.TEMP_USER
                )

            viewModel.uiEvent.test {
                // when
                viewModel.login(idToken)

                // then
                assertEquals(LoginUiEvent.NavigateToSignUp, awaitItem())
            }
        }

    @Test
    fun `login 실패 시 에러 메시지 이벤트가 발생한다`() =
        runTest {
            // given
            val idToken = "test-id-token"
            every { connectivityObserver.value() } returns ConnectivityObserver.Status.Available
            val exception = com.team.domain.model.exception.TodokTodokExceptions.EmptyBodyException
            coEvery { memberRepository.login(idToken) } returns
                com.team.domain.model.exception.NetworkResult.Failure(exception)

            viewModel.uiEvent.test {
                // when
                viewModel.login(idToken)

                // then
                assertEquals(LoginUiEvent.ShowErrorMessage(exception), awaitItem())
            }
        }
}
