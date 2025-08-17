package com.team.todoktodok.presentation.view.profile.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.Support
import com.team.domain.model.member.MemberDiscussion
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
        viewModelScope.launch {
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
                    participatedDiscussions as List<MemberDiscussion>,
                    createdDiscussions as List<MemberDiscussion>,
                )
        }
    }

    fun loadProfile(id: MemberId): Deferred<Profile> =
        viewModelScope.async {
            memberRepository.getProfile(id)
        }

    fun loadActivatedBooks(id: MemberId): Deferred<List<Book>> =
        viewModelScope.async {
            memberRepository.getMemberBooks(id)
        }

    fun loadParticipatedDiscussions(id: MemberId): Deferred<List<MemberDiscussion>> =
        viewModelScope.async {
            memberRepository.getMemberDiscussionRooms(id, MemberDiscussionType.PARTICIPATED)
        }

    fun loadCreatedDiscussions(id: MemberId): Deferred<List<MemberDiscussion>> =
        viewModelScope.async {
            memberRepository.getMemberDiscussionRooms(id, MemberDiscussionType.CREATED)
        }

    fun supportMember(type: Support) {
        viewModelScope.launch {
            val memberId = _uiState.value?.memberId
            if (memberId is MemberId.OtherUser) {
                memberRepository.supportMember(memberId, type)
                onUiEvent(ProfileUiEvent.OnCompleteSupport(type))
            }
        }
    }

    fun refreshProfile() {
        val memberId = _uiState.value?.memberId ?: return
        viewModelScope.launch {
            val profile = loadProfile(memberId)
            _uiState.value = _uiState.value?.modifyProfile(profile.await())
        }
    }

    private fun onUiEvent(event: ProfileUiEvent) {
        _uiEvent.setValue(event)
    }
}
