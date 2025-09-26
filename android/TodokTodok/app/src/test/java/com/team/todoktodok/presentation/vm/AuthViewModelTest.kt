package com.team.todoktodok.presentation.vm

import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.ext.getOrAwaitValue
import com.team.todoktodok.presentation.xml.auth.login.LoginUiEvent
import com.team.todoktodok.presentation.xml.auth.vm.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setup() {
        memberRepository = mockk()
        tokenRepository = mockk()
        notificationRepository = mockk()

        coEvery { tokenRepository.getMemberId() } returns 0L
        coEvery { notificationRepository.registerPushNotification() } returns NetworkResult.Success(Unit)

        viewModel = AuthViewModel(memberRepository, tokenRepository, notificationRepository)
    }

    @Test
    fun `memberId가 존재할 경우 메인 화면으로 이동 이벤트가 발생한다`() =
        runTest {
            coEvery { notificationRepository.registerPushNotification() } returns NetworkResult.Success(Unit)

            viewModel.checkMember()

            assertEquals(LoginUiEvent.NavigateToMain, viewModel.uiEvent.getOrAwaitValue())
        }

    @Test
    fun `memberId가 0이면 로그인 버튼 표시 이벤트가 발생한다`() =
        runTest {
            coEvery { tokenRepository.getMemberId() } returns 0L

            viewModel.checkMember()

            assertEquals(LoginUiEvent.ShowLoginButton, viewModel.uiEvent.getOrAwaitValue())
        }
}
