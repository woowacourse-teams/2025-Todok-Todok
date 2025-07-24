package com.example.todoktodok.presentation.view.auth.vm

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Member
import com.example.domain.model.member.MemberType
import com.example.domain.model.member.MemberType.Companion.MemberType
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.auth.login.LoginUiEvent
import kotlinx.coroutines.launch

class AuthViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private var member = Member(NOT_EXIST_NICKNAME, NOT_EXIST_PROFILE_IMAGE, NOT_EXIST_EMAIL)

    private val _uiEvent: MutableSingleLiveData<LoginUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<LoginUiEvent> get() = _uiEvent

    fun login(
        email: String,
        nickname: String?,
        profileImage: Uri?,
    ) {
        setUpMember(email, nickname, profileImage)
        viewModelScope.launch {
            val result = memberRepository.login(email)
            val role = MemberType(result)
            Log.d("dsadas", "login: $role")

            when (role) {
                MemberType.USER -> onUiEvent(LoginUiEvent.NavigateToMain)
                MemberType.TEMP_USER -> onUiEvent(LoginUiEvent.NavigateToSignUp)
            }
        }
    }

    private fun setUpMember(
        email: String,
        nickname: String?,
        profileImage: Uri?,
    ) {
        member =
            Member(
                nickname ?: NOT_EXIST_NICKNAME,
                profileImage?.toString() ?: NOT_EXIST_PROFILE_IMAGE,
                email,
            )
    }

    fun signUp(
        email: String,
        nickname: String,
        profileImage: String,
    ) {
        viewModelScope.launch {
            memberRepository.signUp(
                Member(
                    email,
                    nickname,
                    profileImage,
                ),
            )
        }
    }

    private fun onUiEvent(event: LoginUiEvent) {
        _uiEvent.setValue(event)
    }

    companion object {
        private const val NOT_EXIST_NICKNAME = ""
        private const val NOT_EXIST_PROFILE_IMAGE = ""
        private const val NOT_EXIST_EMAIL = ""
    }
}
