package com.team.todoktodok.presentation.utview.discussions.all.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.utview.discussions.all.AllDiscussionUiEvent
import kotlinx.coroutines.launch

class AllDiscussionViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<List<Discussion>>(emptyList())
    val uiState: LiveData<List<Discussion>> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<AllDiscussionUiEvent>()
    val uiEvent: SingleLiveData<AllDiscussionUiEvent> get() = _uiEvent

    init {
        loadAllDiscussion()
    }

    private fun loadAllDiscussion() {
        viewModelScope.launch {
            _uiState.value = discussionRepository.getDiscussions()
        }
    }

    fun findDiscussion(index: Int) {
        _uiState.value?.get(index)?.let {
            onUiEvent(AllDiscussionUiEvent.NavigateToDetail(it.id))
        }
    }

    private fun onUiEvent(event: AllDiscussionUiEvent) {
        _uiEvent.setValue(event)
    }
}
