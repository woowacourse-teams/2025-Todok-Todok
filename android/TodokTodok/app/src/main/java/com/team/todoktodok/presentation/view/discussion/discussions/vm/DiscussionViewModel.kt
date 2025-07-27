package com.team.todoktodok.presentation.view.discussion.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussion.discussions.DiscussionUiEvent
import kotlinx.coroutines.launch

class DiscussionViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _discussions = MutableLiveData<List<Discussion>>()
    val discussions: LiveData<List<Discussion>> = _discussions

    private val _uiEvent = MutableSingleLiveData<DiscussionUiEvent>()
    val uiEvent: SingleLiveData<DiscussionUiEvent> = _uiEvent

    init {
        loadDiscussions()
    }

    fun onUiEvent(uiEvent: DiscussionUiEvent) {
        _uiEvent.postValue(uiEvent)
    }

    fun loadDiscussions() {
        viewModelScope.launch {
            _discussions.value = discussionRepository.getDiscussions()
        }
    }
}
