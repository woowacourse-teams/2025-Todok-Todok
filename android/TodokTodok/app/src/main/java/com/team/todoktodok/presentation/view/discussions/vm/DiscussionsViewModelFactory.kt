package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository

class DiscussionsViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionsViewModel::class.java)) {
            return DiscussionsViewModel(discussionRepository, memberRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
