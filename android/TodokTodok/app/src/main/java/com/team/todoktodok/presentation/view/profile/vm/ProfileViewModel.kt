package com.team.todoktodok.presentation.view.profile.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.profile.ProfileUiEvent
import com.team.todoktodok.presentation.view.profile.ProfileUiState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(ProfileUiState())
    val uiState: LiveData<ProfileUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<ProfileUiEvent>()
    val uiEvent: SingleLiveData<ProfileUiEvent> get() = _uiEvent

    fun setMemberId(id: Long) {
        val memberID = MemberId(id)
        _uiState.value = _uiState.value?.copy(memberId = memberID)
    }

    @Suppress("UNCHECKED_CAST")
    fun initState() {
        val memberId = _uiState.value?.memberId ?: return
        withLoading {
            val (profile, books, participatedDiscussions, createdDiscussions) =
                listOf(
                    loadProfile(memberId),
                    loadActivatedBooks(memberId),
                    loadParticipatedDiscussions(memberId),
                    loadCreatedDiscussions(memberId),
                ).awaitAll()

            _uiState.value =
                ProfileUiState.initial(
                    memberId,
                    profile as Profile,
                    books as List<Book>,
                    participatedDiscussions as List<Discussion>,
                    createdDiscussions as List<Discussion>,
                )
        }
    }

    private fun loadProfile(id: MemberId): Deferred<Profile> =
        viewModelScope.async {
            when (val result = memberRepository.getProfile(id)) {
                is NetworkResult.Success -> result.data
                is NetworkResult.Failure -> {
                    onUiEvent(ProfileUiEvent.ShowErrorMessage(result.exception))
                    Profile.EMPTY
                }
            }
        }

    private fun loadActivatedBooks(id: MemberId): Deferred<List<Book>> =
        viewModelScope.async {
            when (val result = memberRepository.getMemberBooks(id)) {
                is NetworkResult.Success -> result.data
                is NetworkResult.Failure -> {
                    onUiEvent(ProfileUiEvent.ShowErrorMessage(result.exception))
                    emptyList()
                }
            }
        }

    private fun loadParticipatedDiscussions(id: MemberId): Deferred<List<Discussion>> =
        viewModelScope.async {
            when (
                val result =
                    memberRepository.getMemberDiscussionRooms(id, MemberDiscussionType.PARTICIPATED)
            ) {
                is NetworkResult.Success -> result.data
                is NetworkResult.Failure -> {
                    onUiEvent(ProfileUiEvent.ShowErrorMessage(result.exception))
                    emptyList()
                }
            }
        }

    private fun loadCreatedDiscussions(id: MemberId): Deferred<List<Discussion>> =
        viewModelScope.async {
            when (
                val result =
                    memberRepository.getMemberDiscussionRooms(id, MemberDiscussionType.CREATED)
            ) {
                is NetworkResult.Success -> result.data
                is NetworkResult.Failure -> {
                    onUiEvent(ProfileUiEvent.ShowErrorMessage(result.exception))
                    emptyList()
                }
            }
        }

    fun supportMember(
        type: Support,
        reason: String,
    ) {
        withLoading {
            val memberId = _uiState.value?.memberId
            if (memberId is MemberId.OtherUser) {
                memberRepository
                    .supportMember(memberId, type, reason)
                    .onSuccess { onUiEvent(ProfileUiEvent.OnCompleteSupport(type)) }
                    .onFailure { onUiEvent(ProfileUiEvent.ShowErrorMessage(it)) }
            }
        }
    }

    fun refreshProfile() {
        val memberId = _uiState.value?.memberId ?: return
        withLoading {
            val profile = loadProfile(memberId)
            _uiState.value = _uiState.value?.modifyProfile(profile.await())
        }
    }

    private fun withLoading(action: suspend () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.toggleLoading(true)
            action()
            _uiState.value = _uiState.value?.toggleLoading(false)
        }
    }

    private fun onUiEvent(event: ProfileUiEvent) {
        _uiEvent.setValue(event)
    }
}
