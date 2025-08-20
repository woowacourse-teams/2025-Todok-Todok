package com.team.todoktodok.presentation.view.discussions.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val discussionRepository: DiscussionRepository,
    private val memberRepository: MemberRepository,
    private val fakeDiscussionRepository: DiscussionRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(DiscussionsUiState())
    val uiState: LiveData<DiscussionsUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<DiscussionsUiEvent>()
    val uiEvent: SingleLiveData<DiscussionsUiEvent> get() = _uiEvent

    private var loadJob: Job? = null

    fun updateTab(
        newFilter: DiscussionFilter,
        duration: Long,
    ) {
        _uiState.value = _uiState.value?.copy(filter = newFilter)

        loadJob?.cancel()
        loadJob =
            viewModelScope.launch {
                delay(duration)
            }
    }

    fun loadSearchedDiscussions(keyword: String) {
        _uiState.value = _uiState.value?.copy(searchKeyword = keyword)
    }

    fun loadLatestDiscussions() {
        val currentUiState = _uiState.value ?: return

        if (currentUiState.latestPageHasNext) {
            withLoading {
                val cursor = _uiState.value?.latestPage?.nextCursor
                when (val result = fakeDiscussionRepository.getLatestDiscussions(cursor = cursor)) {
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
            val tasks =
                MemberDiscussionType.entries.map { type ->
                    viewModelScope.async {
                        type to memberRepository.getMemberDiscussionRooms(MemberId.Mine, type)
                    }
                }

            val results: Map<MemberDiscussionType, NetworkResult<List<MemberDiscussion>>> =
                tasks.awaitAll().toMap()

            results.values.firstOrNull { it is NetworkResult.Failure }?.let { failure ->
                val exception = (failure as NetworkResult.Failure).exception
                onUiEvent(DiscussionsUiEvent.ShowErrorMessage(exception))
                return@withLoading
            }

            val created = (results[MemberDiscussionType.CREATED] as NetworkResult.Success).data
            val participated =
                (results[MemberDiscussionType.PARTICIPATED] as NetworkResult.Success).data

            _uiState.value = _uiState.value?.addMyDiscussion(created, participated)
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
