package com.team.todoktodok.presentation.xml.auth.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.MemberType.Companion.MemberType
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.base.BaseViewModel
import com.team.todoktodok.presentation.xml.auth.login.LoginUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
        private val tokenRepository: TokenRepository,
        connectivityObserver: ConnectivityObserver,
    ) : BaseViewModel(connectivityObserver) {
        private val _uiEvent: Channel<LoginUiEvent> = Channel(Channel.BUFFERED)
        val uiEvent: Flow<LoginUiEvent> get() = _uiEvent.receiveAsFlow()

        init {
            checkMember()
        }

        fun checkMember() {
            viewModelScope.launch {
                val memberId = tokenRepository.getMemberId()
                when (MemberType(memberId)) {
                    MemberType.USER -> {
                        delay(SPLASH_DURATION)
                        onUiEvent(LoginUiEvent.NavigateToMain)
                    }

                    MemberType.TEMP_USER -> onUiEvent(LoginUiEvent.ShowLoginButton)
                }
            }
        }

        fun login(idToken: String) =
            runAsync(
                key = KEY_LOGIN,
                action = { memberRepository.login(idToken) },
                handleSuccess = { type: MemberType ->
                    when (type) {
                        MemberType.USER -> onUiEvent(LoginUiEvent.NavigateToMain)
                        MemberType.TEMP_USER -> onUiEvent(LoginUiEvent.NavigateToSignUp)
                    }
                },
                handleFailure = {
                    onUiEvent(LoginUiEvent.ShowErrorMessage(it))
                },
            )

        private fun onUiEvent(event: LoginUiEvent) {
            viewModelScope.launch {
                _uiEvent.send(event)
            }
        }

        companion object {
            private const val KEY_LOGIN = "login"
            private const val SPLASH_DURATION = 1500L
        }
    }
