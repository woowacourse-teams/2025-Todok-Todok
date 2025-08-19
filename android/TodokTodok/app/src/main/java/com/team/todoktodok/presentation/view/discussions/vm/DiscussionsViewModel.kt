package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<DiscussionsUiEvent>()
    val uiEvent: SingleLiveData<DiscussionsUiEvent> get() = _uiEvent

    private var loadJob: Job? = null

    fun updateTab(
        newFilter: DiscussionFilter,
        duration: Long,
    ) {
        _uiState.value = _uiState.value?.copy(filter = newFilter)

        loadJob?.cancel()
        loadJob =
            viewModelScope.launch {
                delay(duration)
                loadDiscussions()
            }
    }

    fun loadSearchedDiscussions(keyword: String) {
        _uiState.value = _uiState.value?.copy(searchKeyword = keyword)
        loadDiscussions()
    }

    fun loadDiscussions() {
        val currentState = _uiState.value ?: return
        val currentFilter = currentState.filter
        val keyword = currentState.searchKeyword

        setLoading()

        viewModelScope.launch {
            for (filter in DiscussionFilter.entries) {
                // TODO("API 완성시 수정")
                if (filter == DiscussionFilter.HOT) continue
                discussionRepository
                    .getDiscussions(filter, keyword)
                    .onSuccess {
                        updateDiscussions(filter, it)
                        if (filter == currentFilter) onUiEvent(it, filter)
                    }.onFailure {
                        onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it))
                    }
            }
            setLoading()
        }
    }

    private fun updateDiscussions(
        filter: DiscussionFilter,
        discussions: List<Discussion>,
    ) {
        _uiState.value =
            _uiState.value?.let {
                when (filter) {
                    DiscussionFilter.ALL -> it.copy(allDiscussions = discussions)
                    DiscussionFilter.MINE -> it.copy(myDiscussions = discussions)
                    DiscussionFilter.HOT -> it.copy(myDiscussions = discussions)
                }
            }
    }

    private fun setLoading() {
        _uiState.value = _uiState.value?.toggleLoading()
    }

    private fun onUiEvent(event: DiscussionsUiEvent) {
        _uiEvent.setValue(event)
    }

    private fun onUiEvent(
        discussions: List<Discussion>,
        filter: DiscussionFilter,
    ) {
        val event =
            when {
                discussions.isEmpty() && filter == DiscussionFilter.ALL -> DiscussionsUiEvent.ShowNotHasAllDiscussions
                discussions.isEmpty() && filter == DiscussionFilter.MINE -> DiscussionsUiEvent.ShowNotHasMyDiscussions
                filter == DiscussionFilter.ALL -> DiscussionsUiEvent.ShowHasAllDiscussions
                filter == DiscussionFilter.MINE -> DiscussionsUiEvent.ShowHasMyDiscussions
                else -> throw IllegalArgumentException()
            }

        _uiEvent.setValue(event)
    }
}
