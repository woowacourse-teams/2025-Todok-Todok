package com.team.todoktodok.presentation.utview.discussions.all.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.DiscussionRepository

class AllDiscussionViewModelFactory(
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllDiscussionViewModel::class.java)) {
            return AllDiscussionViewModel(discussionRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
