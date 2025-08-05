package com.team.todoktodok.presentation.view.profile.created.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository

class CreatedDiscussionsViewModelFactory(
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatedDiscussionsViewModel::class.java)) {
            return CreatedDiscussionsViewModel(memberRepository) as T
        }
        throw IllegalArgumentException()
    }
}
