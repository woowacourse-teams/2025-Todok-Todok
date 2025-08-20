package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.data.datasource.discussion.FakeDiscussionDataSource
import com.team.todoktodok.data.repository.FakeDiscussionRepository

class DiscussionsViewModelFactory(
    private val discussionRepository: DiscussionRepository,
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscussionsViewModel::class.java)) {
            val fakeDiscussionDatasource = FakeDiscussionDataSource()
            val fakeDiscussionRepository = FakeDiscussionRepository(fakeDiscussionDatasource)
            return DiscussionsViewModel(discussionRepository, memberRepository, fakeDiscussionRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
