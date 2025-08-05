package com.team.todoktodok.presentation.view.profile.joined.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository

class JoinedDiscussionsViewModelFactory(
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JoinedDiscussionsViewModel::class.java)) {
            return JoinedDiscussionsViewModel(memberRepository) as T
        }
        throw IllegalArgumentException()
    }
}
