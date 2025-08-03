package com.team.todoktodok.presentation.view.profile.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository

class ProfileViewModelFactory(
    private val memberRepository: MemberRepository,
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                memberRepository,
                discussionRepository,
            ) as T
        }
        throw IllegalArgumentException()
    }
}
