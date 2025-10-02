package com.team.todoktodok.presentation.compose.discussion.all.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionUiEvent
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllDiscussionViewModel(
    private val discussionRepository: DiscussionRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val _uiState = MutableStateFlow(AllDiscussionUiState())
    val uiState: StateFlow<AllDiscussionUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<AllDiscussionUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadSearchedDiscussions() {
        val keyword = _uiState.value.searchDiscussion.type.keyword
        if (keyword.isBlank()) return

        runAsync(
            key = KEY_SEARCH_DISCUSSIONS,
            action = { discussionRepository.getSearchDiscussion(keyword) },
            handleSuccess = { result ->
                _uiState.update { it.addSearchDiscussion(keyword, result) }
            },
            handleFailure = { onUiEvent(AllDiscussionUiEvent.ShowErrorMessage(it)) },
        )
    }

    fun loadLatestDiscussions() {
        val currentState = _uiState.value.latestDiscussion
        if (!currentState.hasNext || (isLoading.value)) return
        val cursor = currentState.nextCursor

        runAsync(
            key = KEY_LATEST_DISCUSSIONS,
            action = { discussionRepository.getLatestDiscussions(cursor = cursor) },
            handleSuccess = { result ->
                _uiState.update { it.appendLatestDiscussion(result) }
            },
            handleFailure = { onUiEvent(AllDiscussionUiEvent.ShowErrorMessage(it)) },
        )
    }

    fun refreshLatestDiscussions() {
        _uiState.update { it.refresh() }
        loadLatestDiscussions()
    }

    fun clearSearchResult() {
        _uiState.update { it.clearSearchDiscussion() }
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.update { it.removeDiscussion(discussionId) }
    }

    fun modifyKeyword(keyword: String) {
        _uiState.update { it.modifyKeyword(keyword) }
        if (keyword.isBlank()) clearSearchResult()
    }

    private fun onUiEvent(event: AllDiscussionUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_LATEST_DISCUSSIONS = "LATEST_DISCUSSIONS"
        private const val KEY_SEARCH_DISCUSSIONS = "SEARCH_DISCUSSIONS"
    }
}
