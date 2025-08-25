package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiState
import com.team.todoktodok.presentation.view.serialization.SerializationDiscussion
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<DiscussionsUiEvent>()
    val uiEvent: SingleLiveData<DiscussionsUiEvent> get() = _uiEvent

    fun loadSearchedDiscussions(keyword: String) {
        withLoading {
            discussionRepository
                .getSearchDiscussion(keyword)
                .onSuccess {
                    clearSearchResult()
                    _uiState.value = _uiState.value?.addSearchDiscussion(keyword, it)
                    onUiEvent(DiscussionsUiEvent.ShowSearchResult)
                }.onFailure {
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it))
                }
        }
    }

    fun loadHotDiscussions() =
        withLoading {
            coroutineScope {
                val hotDeferred = async { discussionRepository.getHotDiscussion() }
                val activatedDeferred = async { discussionRepository.getActivatedDiscussion() }

                val hotDiscussionResult = hotDeferred.await()
                val activatedDiscussionResult = activatedDeferred.await()

                when {
                    hotDiscussionResult is NetworkResult.Failure -> {
                        onUiEvent(DiscussionsUiEvent.ShowErrorMessage(hotDiscussionResult.exception))
                    }

                    activatedDiscussionResult is NetworkResult.Failure -> {
                        onUiEvent(DiscussionsUiEvent.ShowErrorMessage(activatedDiscussionResult.exception))
                    }

                    else -> {
                        val hotDiscussions = (hotDiscussionResult as NetworkResult.Success).data
                        val activatedDiscussion =
                            (activatedDiscussionResult as NetworkResult.Success).data
                        _uiState.value =
                            _uiState.value?.addHotDiscussion(hotDiscussions, activatedDiscussion)
                    }
                }
            }
        }

    fun loadLatestDiscussions() {
        val state = _uiState.value ?: return
        if (!state.latestPageHasNext) return

        val cursor = state.latestPageNextCursor

        if (state.latestPageHasNext) {
            withLoading {
                when (val result = discussionRepository.getLatestDiscussions(cursor = cursor)) {
                    is NetworkResult.Success -> {
                        _uiState.value = _uiState.value?.addLatestDiscussion(result.data)
                    }

                    is NetworkResult.Failure -> {
                        onUiEvent(DiscussionsUiEvent.ShowErrorMessage(result.exception))
                    }
                }
            }
        }
    }

    fun loadMyDiscussions() =
        withLoading {
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
                        _uiState.value = _uiState.value?.addMyDiscussion(created, participated)
                    }
                }
            }
        }

    private fun loadCreatedDiscussions() =
        viewModelScope.async {
            memberRepository.getMemberDiscussionRooms(
                MemberId.Mine,
                MemberDiscussionType.CREATED,
            )
        }

    private fun loadParticipatedDiscussions() =
        viewModelScope.async {
            memberRepository.getMemberDiscussionRooms(
                MemberId.Mine,
                MemberDiscussionType.PARTICIPATED,
            )
        }

    fun loadActivatedDiscussions() {
        val activatedPage = _uiState.value?.hotDiscussion?.activatedPage
        val cursor = activatedPage?.nextCursor ?: return
        if (!activatedPage.hasNext) return

        withLoading {
            discussionRepository
                .getActivatedDiscussion(cursor = cursor)
                .onSuccess {
                    _uiState.value = _uiState.value?.appendActivatedDiscussion(it)
                }.onFailure {
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it))
                }
        }
    }

    fun clearSearchResult() {
        _uiState.value = _uiState.value?.clearSearchDiscussion()
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.value = _uiState.value?.removeDiscussion(discussionId)
    }

    fun modifyDiscussion(discussion: SerializationDiscussion) {
        _uiState.value = _uiState.value?.modifyDiscussion(discussion.toDomain())
    }

    private fun withLoading(action: suspend () -> Unit) {
        viewModelScope.launch {
            setLoading()
            action()
            setLoading()
        }
    }

    private fun setLoading() {
        _uiState.value = _uiState.value?.toggleLoading()
    }

    private fun onUiEvent(event: DiscussionsUiEvent) {
        _uiEvent.setValue(event)
    }
}
