package com.team.todoktodok.presentation.view.auth.signup.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.exception.onSuccessSuspend
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.auth.signup.SignUpUiEvent
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val memberRepository: MemberRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _uiEvent = MutableSingleLiveData<SignUpUiEvent>()
    val uiEvent: SingleLiveData<SignUpUiEvent> get() = _uiEvent

    fun submitNickname(text: String) {
        runCatching {
            Nickname(text)
        }.onSuccess { nickname ->
            signUp(nickname)
        }.onFailure { e ->
            _uiEvent.setValue(SignUpUiEvent.ShowInvalidNickNameMessage(e as NickNameException))
        }
    }

    private fun signUp(nickname: Nickname) {
        viewModelScope.launch {
            _isLoading.value = true
            memberRepository
                .signUp(nickname.value)
                .onSuccessSuspend {
                    registerPushNotification()
                    _uiEvent.setValue(SignUpUiEvent.NavigateToMain)
                }.onFailure { _uiEvent.setValue(SignUpUiEvent.ShowErrorMessage(it)) }
            _isLoading.value = false
        }
    }

    private suspend fun registerPushNotification() {
        notificationRepository.registerPushNotification().onSuccess { }.onFailure { }
    }
}
