package com.team.todoktodok.presentation.utview.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.DiscussionFilter
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.utview.discussions.DiscussionsUiState
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    init {
        loadDiscussions()
    }

    fun loadDiscussions() {
        DiscussionFilter.entries.forEach { filter ->
            loadFilteredDiscussions(filter)
        }
    }

    fun loadSearchedDiscussions(keyword: String) {
        DiscussionFilter.entries.forEach { filter ->
            loadFilteredDiscussions(filter, keyword)
        }
    }

    private fun loadFilteredDiscussions(
        filter: DiscussionFilter,
        keyword: String? = null,
    ) {
        viewModelScope.launch {
            val discussions = discussionRepository.getDiscussions(filter, keyword)
            val currentState = _uiState.value ?: DiscussionsUiState()

            val newState =
                when (filter) {
                    DiscussionFilter.ALL -> currentState.copy(allDiscussions = discussions)
                    DiscussionFilter.MINE -> currentState.copy(myDiscussions = discussions)
                }

            _uiState.value = newState
        }
    }

    fun updateTab(tab: DiscussionFilter) {
        _uiState.value = _uiState.value?.copy(tab = tab) ?: DiscussionsUiState(tab = tab)
    }
}
