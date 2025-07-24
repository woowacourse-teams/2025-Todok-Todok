package com.example.todoktodok.presentation.view.auth.signup.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.member.NickNameException
import com.example.domain.model.member.Nickname
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.auth.signup.SignUpUiEvent
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
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
            memberRepository.signUp(nickname.value)
            _uiEvent.setValue(SignUpUiEvent.NavigateToMain)
        }
    }
}
