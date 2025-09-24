package com.team.todoktodok.presentation.xml.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.presentation.core.base.BaseViewModel
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.xml.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.xml.discussions.DiscussionsUiState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
    private val memberRepository: MemberRepository,
    private val notificationRepository: NotificationRepository,
    networkConnectivityObserver: ConnectivityObserver,
) : BaseViewModel(networkConnectivityObserver) {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<DiscussionsUiEvent>()
    val uiEvent: SingleLiveData<DiscussionsUiEvent> get() = _uiEvent

    init {
        loadIsUnReadNotification()
    }

    fun loadIsUnReadNotification() {
        viewModelScope.launch {
            notificationRepository
                .getUnreadNotificationsCount()
                .onSuccess { isExist ->
                    _uiState.value = _uiState.value?.changeUnreadNotification(isExist)
                }.onFailure { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) }
        }
    }

    fun loadSearchedDiscussions(keyword: String) =
        runAsync(
            key = KEY_SEARCH_DISCUSSIONS,
            action = { discussionRepository.getSearchDiscussion(keyword) },
            handleSuccess = { _uiState.value = _uiState.value?.addSearchDiscussion(keyword, it) },
            handleFailure = { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) },
        )

    fun loadHotDiscussions() {
        viewModelScope.launch {
            val hotResult = loadHotDiscussion().await()
            val activatedResult = loadActivatedDiscussion().await()
            when {
                hotResult is NetworkResult.Failure -> {
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(hotResult.exception))
                }

                activatedResult is NetworkResult.Failure -> {
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(activatedResult.exception))
                }

                else -> {
                    val hot = (hotResult as NetworkResult.Success).data
                    val activated = (activatedResult as NetworkResult.Success).data
                    _uiState.value = _uiState.value?.addHotDiscussion(hot, activated)
                }
            }
        }
    }

    private fun loadHotDiscussion(): Deferred<NetworkResult<List<Discussion>>> =
        runAsyncWithResult(
            key = KEY_HOT_DISCUSSIONS,
            action = { discussionRepository.getHotDiscussion() },
        )

    private fun loadActivatedDiscussion(): Deferred<NetworkResult<ActivatedDiscussionPage>> =
        runAsyncWithResult(
            key = KEY_ACTIVATED_DISCUSSIONS,
            action = { discussionRepository.getActivatedDiscussion() },
        )

    fun loadLatestDiscussions() {
        val currentState = _uiState.value ?: DiscussionsUiState()
        if (!currentState.latestPageHasNext) return
        val cursor = currentState.latestPageNextCursor

        runAsync(
            key = KEY_LATEST_DISCUSSIONS,
            action = { discussionRepository.getLatestDiscussions(cursor = cursor) },
            handleSuccess = { _uiState.value = _uiState.value?.addLatestDiscussion(it) },
            handleFailure = { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) },
        )
    }

    fun loadMyDiscussions() {
        viewModelScope.launch {
            coroutineScope {
                val createdDeferred = loadCreatedDiscussions()
                val participatedDeferred = loadParticipatedDiscussions()

                val createdResult = createdDeferred.await()
                val participatedResult = participatedDeferred.await()

                when {
                    createdResult is NetworkResult.Failure -> {
                        onUiEvent(DiscussionsUiEvent.ShowErrorMessage(createdResult.exception))
                    }

                    participatedResult is NetworkResult.Failure -> {
                        onUiEvent(DiscussionsUiEvent.ShowErrorMessage(participatedResult.exception))
                    }

                    else -> {
                        val created = (createdResult as NetworkResult.Success).data
                        val participated = (participatedResult as NetworkResult.Success).data
                        _uiState.value =
                            _uiState.value?.addMyDiscussion(created, participated)
                    }
                }
            }
        }
    }

    private fun loadCreatedDiscussions(): Deferred<NetworkResult<List<Discussion>>> =
        runAsyncWithResult(
            key = KEY_MY_CREATED_DISCUSSIONS,
            action = {
                memberRepository.getMemberDiscussionRooms(
                    MemberId.Mine,
                    MemberDiscussionType.CREATED,
                )
            },
        )

    private fun loadParticipatedDiscussions(): Deferred<NetworkResult<List<Discussion>>> =
        runAsyncWithResult(
            key = KEY_MY_PARTICIPATED_DISCUSSIONS,
            action = {
                memberRepository.getMemberDiscussionRooms(
                    MemberId.Mine,
                    MemberDiscussionType.PARTICIPATED,
                )
            },
        )

    fun loadActivatedDiscussions() {
        val activatedPage = _uiState.value?.hotDiscussion?.activatedPage ?: return
        if (!activatedPage.hasNext) return
        val cursor = activatedPage.nextCursor ?: return

        runAsync(
            key = KEY_ACTIVATED_DISCUSSIONS,
            action = { discussionRepository.getActivatedDiscussion(cursor = cursor) },
            handleSuccess = { _uiState.value = _uiState.value?.appendActivatedDiscussion(it) },
            handleFailure = { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) },
        )
    }

    fun modifyDiscussion(discussionId: Long) {
        runAsync(
            key = KEY_MODIFY_DISCUSSION,
            action = { discussionRepository.getDiscussion(discussionId) },
            handleSuccess = { _uiState.value = _uiState.value?.modifyDiscussion(it) },
            handleFailure = { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) },
        )
        clearSearchResult()
        onUiEvent(DiscussionsUiEvent.ClearSearchResult)
    }

    fun refreshLatestDiscussions() {
        _uiState.value = _uiState.value?.clearLatestDiscussion()
        loadLatestDiscussions()
    }

    fun clearSearchResult() {
        _uiState.value = _uiState.value?.clearSearchDiscussion()
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.value = _uiState.value?.removeDiscussion(discussionId)
    }

    private fun onUiEvent(event: DiscussionsUiEvent) {
        _uiEvent.setValue(event)
    }

    companion object {
        private const val KEY_SEARCH_DISCUSSIONS = "SEARCH_DISCUSSIONS"
        private const val KEY_HOT_DISCUSSIONS = "HOT_DISCUSSIONS"
        private const val KEY_ACTIVATED_DISCUSSIONS = "ACTIVATED_DISCUSSIONS"
        private const val KEY_LATEST_DISCUSSIONS = "LATEST_DISCUSSIONS"
        private const val KEY_MY_CREATED_DISCUSSIONS = "MY_CREATED_DISCUSSIONS"
        private const val KEY_MY_PARTICIPATED_DISCUSSIONS = "MY_PARTICIPATED_DISCUSSIONS"
        private const val KEY_MODIFY_DISCUSSION = "MODIFY_DISCUSSION"
    }
}
