package com.team.todoktodok.presentation.xml.discussion.create.draft.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.DiscussionRepository

class DraftsViewModelFactory(
    private val discussionRepository: DiscussionRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DraftsViewModel::class.java)) {
            return DraftsViewModel(
                discussionRepository,
            ) as T
        } else {
            throw IllegalArgumentException("알 수 없는 ViewModel 클래스입니다: ${modelClass.name}")
        }
    }
}