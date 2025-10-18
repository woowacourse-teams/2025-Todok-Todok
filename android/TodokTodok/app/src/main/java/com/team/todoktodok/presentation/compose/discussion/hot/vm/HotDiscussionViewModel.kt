package com.team.todoktodok.presentation.compose.discussion.hot.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionUiEvent
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HotDiscussionViewModel(
    private val discussionRepository: DiscussionRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val _uiState = MutableStateFlow(HotDiscussionUiState())
    val uiState: StateFlow<HotDiscussionUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<HotDiscussionUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadHotDiscussions() {
        viewModelScope.launch {
            loadPopularDiscussion()
            loadActivatedDiscussions(true)
        }
    }

    private fun loadPopularDiscussion() =
        runAsync(
            key = KEY_POPULAR_DISCUSSIONS,
            action = { discussionRepository.getHotDiscussion() },
            handleSuccess = { result -> _uiState.update { it.addPopularDiscussions(result) } },
            handleFailure = {
                _uiState.update { state -> state.copy(isRefreshing = false) }
                onUiEvent(HotDiscussionUiEvent.ShowErrorMessage(it))
            },
        )

    fun loadActivatedDiscussions(initial: Boolean = false) {
        val current = _uiState.value

        if (!initial && (!current.hasNextPage || current.activatedDiscussions.notHasDiscussion)) return

        val cursor = if (initial) null else current.activatedDiscussions.pageInfo.nextCursor

        runAsync(
            key = KEY_ACTIVATED_DISCUSSIONS,
            action = { discussionRepository.getActivatedDiscussion(cursor = cursor) },
            handleSuccess = { page ->
                _uiState.update { it.appendActivatedDiscussion(page) }
            },
            handleFailure = {
                _uiState.update { state -> state.copy(isRefreshing = false) }
                onUiEvent(HotDiscussionUiEvent.ShowErrorMessage(it))
            },
        )
    }

    fun modifyDiscussion(discussion: SerializationDiscussion) {
        _uiState.update { it.modifyDiscussion(discussion) }
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.update { it.removeDiscussion(discussionId) }
    }

    fun refreshHotDiscussions() {
        _uiState.update { it.clearForRefresh() }
        loadHotDiscussions()
    }

    private fun onUiEvent(event: HotDiscussionUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    companion object {
        private const val KEY_POPULAR_DISCUSSIONS = "POPULAR_DISCUSSIONS"
        private const val KEY_ACTIVATED_DISCUSSIONS = "ACTIVATED_DISCUSSIONS"
    }
}
