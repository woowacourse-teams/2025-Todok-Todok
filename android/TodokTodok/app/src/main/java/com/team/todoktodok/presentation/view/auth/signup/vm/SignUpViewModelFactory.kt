package com.team.todoktodok.presentation.view.auth.signup.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository

class SignUpViewModelFactory(
    private val memberRepository: MemberRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(memberRepository, notificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
