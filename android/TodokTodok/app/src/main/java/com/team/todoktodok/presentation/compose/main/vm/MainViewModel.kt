package com.team.todoktodok.presentation.compose.main.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.presentation.compose.main.MainDestination
import com.team.todoktodok.presentation.compose.main.MainUiEvent
import com.team.todoktodok.presentation.compose.main.MainUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val discussionRepository: DiscussionRepository,
    private val notificationRepository: NotificationRepository,
    networkConnectivityObserver: ConnectivityObserver,
) : BaseViewModel(networkConnectivityObserver) {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<MainUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun sendPushNotificationToken() {
        viewModelScope.launch {
            notificationRepository
                .registerPushNotification()
                .onFailure { exceptions ->
                    onUiEvent(MainUiEvent.ShowErrorMessage(exceptions))
                }
        }
    }

    fun loadIsUnreadNotification() {
        viewModelScope.launch {
            notificationRepository
                .getUnreadNotificationsCount()
                .onSuccess { isExist ->
                    _uiState.update { it.changeUnreadNotification(isExist) }
                }.onFailure { exceptions ->
                    onUiEvent(MainUiEvent.ShowErrorMessage(exceptions))
                }
        }
    }

    fun loadSearchedDiscussions() {
        val keyword = _uiState.value.searchDiscussion.type.keyword
        if (keyword.isBlank()) return

        runAsync(
            key = KEY_SEARCH_DISCUSSIONS,
            action = { discussionRepository.getSearchDiscussion(keyword) },
            handleSuccess = { result ->
                _uiState.update { it.addSearchDiscussion(keyword, result) }
                onUiEvent(MainUiEvent.ScrollToAllDiscussion)
            },
            handleFailure = { onUiEvent(MainUiEvent.ShowErrorMessage(it)) },
        )
    }

    fun changeSearchBarVisibility() {
        _uiState.update { it.changeSearchBarVisibility() }
    }

    fun changeBottomNavigationTab(destination: MainDestination) {
        _uiState.update { it.changeBottomNavigationTab(destination) }
    }

    fun clearSearchResult() {
        _uiState.update { it.clearSearchDiscussion() }
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.update { it.removeDiscussion(discussionId) }
    }

    fun modifySearchKeyword(keyword: String) {
        _uiState.update { it.modifySearchKeyword(keyword) }
        if (keyword.isBlank()) clearSearchResult()
    }

    fun modifyDiscussion(discussion: SerializationDiscussion) {
        _uiState.update { it.modifyDiscussion(discussion) }
    }

    private fun onUiEvent(event: MainUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_SEARCH_DISCUSSIONS = "SEARCH_DISCUSSIONS"
    }
}
