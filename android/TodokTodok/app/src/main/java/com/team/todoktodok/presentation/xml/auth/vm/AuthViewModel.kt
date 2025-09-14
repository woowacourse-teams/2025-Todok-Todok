package com.team.todoktodok.presentation.xml.auth.vm

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccessSuspend
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.MemberType.Companion.MemberType
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.auth.login.LoginUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel(
    private val memberRepository: MemberRepository,
    private val tokenRepository: TokenRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: MutableLiveData<Boolean> get() = _isLoading

    private val _uiEvent: MutableSingleLiveData<LoginUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<LoginUiEvent> get() = _uiEvent

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

    fun login(
        email: String,
        nickname: String?,
        profileImage: Uri?,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            memberRepository
                .login(
                    email,
                    nickname ?: NOT_EXIST_NICKNAME,
                    profileImage?.toString() ?: NOT_EXIST_PROFILE_IMAGE,
                ).onSuccessSuspend { type: MemberType ->
                    when (type) {
                        MemberType.USER -> {
                            notificationRepository.registerPushNotification()
                            onUiEvent(LoginUiEvent.NavigateToMain)
                        }

                        MemberType.TEMP_USER -> onUiEvent(LoginUiEvent.NavigateToSignUp)
                    }
                }.onFailure {
                    onUiEvent(LoginUiEvent.ShowErrorMessage(it))
                }
            _isLoading.value = false
        }
    }

    private fun onUiEvent(event: LoginUiEvent) {
        _uiEvent.setValue(event)
    }

    companion object {
        private const val NOT_EXIST_NICKNAME = ""
        private const val NOT_EXIST_PROFILE_IMAGE = ""
        private const val SPLASH_DURATION = 1500L
    }
}
