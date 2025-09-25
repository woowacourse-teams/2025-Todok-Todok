package com.team.todoktodok.presentation.xml.auth.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.domain.repository.TokenRepository

class AuthViewModelFactory(
    private val memberRepository: MemberRepository,
    private val tokenRepository: TokenRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(memberRepository, tokenRepository, notificationRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
