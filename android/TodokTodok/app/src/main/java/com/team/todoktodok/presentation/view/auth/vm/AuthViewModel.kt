package com.team.todoktodok.presentation.view.auth.vm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.MemberType.Companion.MemberType
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.auth.login.LoginUiEvent
import kotlinx.coroutines.launch

class AuthViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiEvent: MutableSingleLiveData<LoginUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<LoginUiEvent> get() = _uiEvent

    fun login(
        email: String,
        nickname: String?,
        profileImage: Uri?,
    ) {
        viewModelScope.launch {
            val result =
                memberRepository.login(
                    email,
                    nickname ?: NOT_EXIST_NICKNAME,
                    profileImage?.toString() ?: NOT_EXIST_PROFILE_IMAGE,
                )
            val role = MemberType(result)
            when (role) {
                MemberType.USER -> onUiEvent(LoginUiEvent.NavigateToMain)
                MemberType.TEMP_USER -> onUiEvent(LoginUiEvent.NavigateToSignUp)
            }
        }
    }

    private fun onUiEvent(event: LoginUiEvent) {
        _uiEvent.setValue(event)
    }

    companion object {
        private const val NOT_EXIST_NICKNAME = ""
        private const val NOT_EXIST_PROFILE_IMAGE = ""
    }
}
