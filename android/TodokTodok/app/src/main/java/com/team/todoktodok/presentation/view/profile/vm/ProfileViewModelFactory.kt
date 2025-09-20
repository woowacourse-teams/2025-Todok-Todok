package com.team.todoktodok.presentation.view.profile.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository

class ProfileViewModelFactory(
    private val memberRepository: MemberRepository,
    private val tokenRepository: TokenRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                memberRepository,
                tokenRepository,
            ) as T
        }
        throw IllegalArgumentException()
    }
}
