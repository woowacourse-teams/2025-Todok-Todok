package com.team.todoktodok.presentation.view.profile.vm

import android.content.ContentResolver
import android.net.Uri
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
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.core.ImagePayloadMapper
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.profile.ProfileUiEvent
import com.team.todoktodok.presentation.view.profile.ProfileUiState
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val memberRepository: MemberRepository,
    private val tokenRepository: TokenRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(ProfileUiState())
    val uiState: LiveData<ProfileUiState> get() = _uiState

    private val _uiEvent = MutableSingleLiveData<ProfileUiEvent>()
    val uiEvent: SingleLiveData<ProfileUiEvent> get() = _uiEvent

    @Suppress("UNCHECKED_CAST")
    fun loadProfile(id: Long) {
        viewModelScope.launch {
            val memberID = MemberId(id, tokenRepository.getMemberId())
            _uiState.value = _uiState.value?.copy(memberId = memberID)
            val memberId = _uiState.value?.memberId ?: return@launch
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

    @Suppress("UNCHECKED_CAST")
    fun refreshUserActivities() {
        val memberId = _uiState.value?.memberId ?: return
        withLoading {
            val (books, participatedDiscussions, createdDiscussions) =
                listOf(
                    loadActivatedBooks(memberId),
                    loadParticipatedDiscussions(memberId),
                    loadCreatedDiscussions(memberId),
                ).awaitAll()

            _uiState.value =
                _uiState.value?.modifyActivities(
                    books as List<Book>,
                    participatedDiscussions as List<Discussion>,
                    createdDiscussions as List<Discussion>,
                )
        }
    }

    fun updateProfile(
        imageUri: Uri?,
        contentResolver: ContentResolver,
    ) {
        imageUri ?: return
        viewModelScope.launch {
            when (
                val result =
                    memberRepository.modifyProfileImage(
                        ImagePayloadMapper(contentResolver).from(imageUri),
                    )
            ) {
                is NetworkResult.Failure -> onUiEvent(ProfileUiEvent.ShowErrorMessage(result.exception))
                is NetworkResult.Success -> {
                    val updatedState =
                        _uiState.value?.items?.updateProfileImage(result.data) ?: return@launch
                    _uiState.value = _uiState.value?.copy(items = updatedState)
                }
            }
        }
    }

    private fun List<ProfileItems>.updateProfileImage(newImage: String): List<ProfileItems> =
        map { item ->
            when (item) {
                is ProfileItems.HeaderItem -> item
                is ProfileItems.InformationItem ->
                    ProfileItems.InformationItem(
                        item.value.copy(profileImage = newImage),
                        item.isMyProfile,
                    )

                is ProfileItems.TabItem -> item
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
