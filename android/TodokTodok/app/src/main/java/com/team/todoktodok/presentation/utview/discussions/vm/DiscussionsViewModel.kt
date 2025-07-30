package com.team.todoktodok.presentation.utview.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.utview.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.utview.discussions.DiscussionsUiState
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    private val _uiEvent = MutableLiveData<DiscussionsUiEvent>()
    val uiEvent: LiveData<DiscussionsUiEvent> get() = _uiEvent

    init {
        loadDiscussionsForCurrentTab(null)
    }

    private fun loadDiscussionsForCurrentTab(keywordToSearch: String?) {
        val currentTab = _uiState.value?.tab ?: DiscussionFilter.ALL
        val keyword = keywordToSearch ?: _uiState.value?.searchKeyword

        viewModelScope.launch {
            val discussions = discussionRepository.getDiscussions(currentTab, keyword)

            if (discussions.isEmpty()) {
                handleDiscussionNotExistUiEvent(currentTab)
                return@launch
            }

            handleDiscussionExistUiEvent(currentTab)
            updateUiState(currentTab, discussions)
        }
    }

    private fun handleDiscussionNotExistUiEvent(filter: DiscussionFilter) {
        val event =
            when (filter) {
                DiscussionFilter.ALL -> DiscussionsUiEvent.ShowNotHasAllDiscussions
                DiscussionFilter.MINE -> DiscussionsUiEvent.ShowNotHasMyDiscussions
            }
        _uiEvent.value = event
    }

    private fun handleDiscussionExistUiEvent(filter: DiscussionFilter) {
        val event =
            when (filter) {
                DiscussionFilter.ALL -> DiscussionsUiEvent.ShowHasAllDiscussions
                DiscussionFilter.MINE -> DiscussionsUiEvent.ShowHasMyDiscussions
            }
        _uiEvent.value = event
    }

    private fun updateUiState(
        filter: DiscussionFilter,
        discussions: List<Discussion>,
    ) {
        val currentState = _uiState.value ?: DiscussionsUiState()

        val newState =
            when (filter) {
                DiscussionFilter.ALL -> currentState.copy(allDiscussions = discussions)
                DiscussionFilter.MINE -> currentState.copy(myDiscussions = discussions)
            }
        _uiState.value = newState
    }

    fun loadSearchedDiscussions(keyword: String) {
        _uiState.value = _uiState.value?.copy(searchKeyword = keyword)
        loadDiscussionsForCurrentTab(keyword)
    }

    fun updateTab(newTab: DiscussionFilter) {
        _uiState.value = _uiState.value?.copy(tab = newTab)
        loadDiscussionsForCurrentTab(null)
    }
}
