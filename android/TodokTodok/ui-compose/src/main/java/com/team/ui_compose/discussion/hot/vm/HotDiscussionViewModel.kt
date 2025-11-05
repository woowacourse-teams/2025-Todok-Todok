package com.team.ui_compose.discussion.hot.vm

import androidx.lifecycle.viewModelScope
import com.team.core.base.BaseViewModel
import com.team.domain.ConnectivityObserver
import com.team.domain.repository.DiscussionRepository
import com.team.ui_compose.discussion.hot.HotDiscussionUiEvent
import com.team.ui_compose.discussion.hot.HotDiscussionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotDiscussionViewModel
    @Inject
    constructor(
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
                loadInitialActivatedDiscussions()
            }
        }

        private fun loadPopularDiscussion() =
            runAsync(
                key = KEY_POPULAR_DISCUSSIONS,
                action = { discussionRepository.getHotDiscussion() },
                handleSuccess = { result -> _uiState.update { it.setPopularDiscussions(result) } },
                handleFailure = {
                    _uiState.update { state -> state.copy(isRefreshing = false) }
                    onUiEvent(HotDiscussionUiEvent.ShowErrorMessage(it))
                },
            )

        private fun loadInitialActivatedDiscussions() {
            val currentState = _uiState.value
            if (!currentState.activatedDiscussions.notHasDiscussion) {
                _uiState.update { it.clearForRefresh() }
            }

            runAsync(
                key = KEY_ACTIVATED_DISCUSSIONS,
                action = { discussionRepository.getActivatedDiscussion(cursor = null) },
                handleSuccess = { page ->
                    _uiState.update { it.appendActivatedDiscussion(page) }
                },
                handleFailure = { error ->
                    onUiEvent(HotDiscussionUiEvent.ShowErrorMessage(error))
                },
            )
        }

        fun loadNextActivatedDiscussions() {
            val currentState = _uiState.value
            val hasNextPage = currentState.hasNextPage
            val hasData = !currentState.activatedDiscussions.notHasDiscussion

            if (!hasNextPage || !hasData) return

            val nextCursor = currentState.activatedDiscussions.pageInfo.nextCursor

            runAsync(
                key = KEY_ACTIVATED_DISCUSSIONS,
                action = { discussionRepository.getActivatedDiscussion(cursor = nextCursor) },
                handleSuccess = { page ->
                    _uiState.update { it.appendActivatedDiscussion(page) }
                },
                handleFailure = { error ->
                    onUiEvent(HotDiscussionUiEvent.ShowErrorMessage(error))
                },
            )
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
