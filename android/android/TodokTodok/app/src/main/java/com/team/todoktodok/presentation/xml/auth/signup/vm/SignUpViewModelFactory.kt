package com.team.todoktodok.presentation.xml.auth.signup.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.MemberRepository

class SignUpViewModelFactory(
    private val memberRepository: MemberRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                memberRepository,
                connectivityObserver,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
