package com.team.todoktodok.presentation.xml.auth.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository

class AuthViewModelFactory(
    private val memberRepository: MemberRepository,
    private val tokenRepository: TokenRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                memberRepository,
                tokenRepository,
                connectivityObserver,
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
