package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiState
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    private val _uiEvent = MutableLiveData<DiscussionsUiEvent>()
    val uiEvent: LiveData<DiscussionsUiEvent> get() = _uiEvent

    fun updateTab(newTab: DiscussionFilter) {
        _uiState.value = _uiState.value?.copy(filter = newTab)
        loadDiscussions()
    }

    fun loadSearchedDiscussions(keyword: String) {
        _uiState.value = _uiState.value?.copy(searchKeyword = keyword)
        loadDiscussions()
    }

    fun loadDiscussions() {
        val currentState = _uiState.value ?: return
        val currentFilter = currentState.filter
        val keyword = currentState.searchKeyword

        DiscussionFilter.entries.forEach { filter ->
            /**
             * 토론 개수를 알기 위해 화면에 보이지 않는 탭의 정보까지 호출한다.
             * API 트래픽 최적화를 위해서 필터를 받아 개수를 반환하는 API를 구현하는 것은 어떨까 ?
             *
             * */
            viewModelScope.launch {
                val discussions = discussionRepository.getDiscussions(filter, keyword)
                updateDiscussions(filter, discussions)

                if (filter == currentFilter) {
                    onUiEvent(discussions, filter)
                }
            }
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
                }
            }
    }

    private fun onUiEvent(
        discussions: List<Discussion>,
        filter: DiscussionFilter,
    ) {
        _uiEvent.value =
            when {
                discussions.isEmpty() && filter == DiscussionFilter.ALL -> DiscussionsUiEvent.ShowNotHasAllDiscussions
                discussions.isEmpty() && filter == DiscussionFilter.MINE -> DiscussionsUiEvent.ShowNotHasMyDiscussions
                filter == DiscussionFilter.ALL -> DiscussionsUiEvent.ShowHasAllDiscussions
                filter == DiscussionFilter.MINE -> DiscussionsUiEvent.ShowHasMyDiscussions
                else -> throw IllegalArgumentException()
            }
    }
}
