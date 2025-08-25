package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
                .onSuccess { _uiState.value = _uiState.value?.addSearchDiscussion(keyword, it) }
                .onFailure { onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it)) }
        }
    }

    fun loadHotDiscussions() =
        withLoading {
            coroutineScope {
                val hotDeferred = async { discussionRepository.getHotDiscussion() }
                val activatedDeferred = async { discussionRepository.getActivatedDiscussion() }
                val result = awaitAll(hotDeferred, activatedDeferred)

                (result.firstOrNull { it is NetworkResult.Failure } as? NetworkResult.Failure)?.let {
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(it.exception))
                } ?: run {
                    val hotDiscussions =
                        (result[0] as NetworkResult.Success).data as List<Discussion>
                    val activatedDiscussion =
                        (result[1] as NetworkResult.Success).data as ActivatedDiscussionPage

                    _uiState.value =
                        _uiState.value?.addHotDiscussion(hotDiscussions, activatedDiscussion)
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
                val tasks =
                    MemberDiscussionType.entries.map { type ->
                        viewModelScope.async {
                            type to memberRepository.getMemberDiscussionRooms(MemberId.Mine, type)
                        }
                    }

                val results: Map<MemberDiscussionType, NetworkResult<List<Discussion>>> =
                    tasks.awaitAll().toMap()

                results.values.firstOrNull { it is NetworkResult.Failure }?.let { failure ->
                    val exception = (failure as NetworkResult.Failure).exception
                    onUiEvent(DiscussionsUiEvent.ShowErrorMessage(exception))
                    return@coroutineScope
                }

                val created = (results[MemberDiscussionType.CREATED] as NetworkResult.Success).data
                val participated =
                    (results[MemberDiscussionType.PARTICIPATED] as NetworkResult.Success).data

                _uiState.value = _uiState.value?.addMyDiscussion(created, participated)
            }
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

    fun clearKeyword() {
        _uiState.value = _uiState.value?.clearSearchDiscussion()
    }

    fun removeDiscussion(discussionId: Long) {
        _uiState.value = _uiState.value?.removeDiscussion(discussionId)
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
