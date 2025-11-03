package com.team.todoktodok.presentation.xml.auth.signup.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.base.BaseViewModel
import com.team.todoktodok.presentation.xml.auth.signup.SignUpUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
        connectivityObserver: ConnectivityObserver,
    ) : BaseViewModel(connectivityObserver) {
        private val _uiEvent = Channel<SignUpUiEvent>(Channel.BUFFERED)
        val uiEvent: Flow<SignUpUiEvent> get() = _uiEvent.receiveAsFlow()

        fun submitNickname(text: String) {
            runCatching {
                Nickname(text)
            }.onSuccess { nickname ->
                signUp(nickname)
            }.onFailure { e ->
                onUiEvent(SignUpUiEvent.ShowInvalidNickNameMessage(e as NickNameException))
            }
        }

        private fun signUp(nickname: Nickname) =
            runAsync(
                key = KEY_SIGN_UP,
                action = { memberRepository.signUp(nickname) },
                handleSuccess = { onUiEvent(SignUpUiEvent.NavigateToMain) },
                handleFailure = {
                    onUiEvent(SignUpUiEvent.ShowErrorMessage(it))
                },
            )

        private fun onUiEvent(event: SignUpUiEvent) {
            viewModelScope.launch {
                _uiEvent.send(event)
            }
        }

        companion object {
            private const val KEY_SIGN_UP = "signUp"
        }
    }
