package com.team.todoktodok.presentation.compose.my.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.ImagePayload
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.repository.DiscussionRepository
import com.team.domain.repository.MemberRepository
import com.team.domain.repository.TokenRepository
import com.team.todoktodok.presentation.compose.my.MyProfileUiEvent
import com.team.todoktodok.presentation.compose.my.MyProfileUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val memberRepository: MemberRepository,
    private val discussionRepository: DiscussionRepository,
    private val tokenRepository: TokenRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState: StateFlow<MyProfileUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<MyProfileUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadInitialProfile() {
        loadMyMemberId()
        loadProfile()
        loadMyBooks()
        loadDiscussions()
    }

    fun loadDiscussions() {
        loadLikedDiscussions()
        loadParticipatedDiscussions()
    }

    private fun loadProfile() =
        runAsync(
            key = KEY_FETCH_PROFILE,
            action = { memberRepository.getProfile(MemberId.Mine) },
            handleSuccess = { _uiState.value = _uiState.value.copy(profile = it) },
            handleFailure = { onUiEvent(MyProfileUiEvent.ShowErrorMessage(it)) },
        )

    private fun loadMyBooks() =
        runAsync(
            key = KEY_FETCH_MY_BOOKS,
            action = { memberRepository.getMemberBooks(MemberId.Mine) },
            handleSuccess = { result -> _uiState.update { it.setActivatedBooks(result) } },
            handleFailure = { onUiEvent(MyProfileUiEvent.ShowErrorMessage(it)) },
        )

    private fun loadLikedDiscussions() =
        runAsync(
            key = KEY_FETCH_LIKED_DISCUSSIONS,
            action = { discussionRepository.getLikedDiscussion() },
            handleSuccess = { result -> _uiState.update { it.setLikedDiscussions(result) } },
            handleFailure = { onUiEvent(MyProfileUiEvent.ShowErrorMessage(it)) },
        )

    private fun loadParticipatedDiscussions() =
        runAsync(
            key = KEY_FETCH_PARTICIPATED_DISCUSSIONS,
            action = {
                memberRepository.getMemberDiscussionRooms(
                    id = MemberId.Mine,
                    type = MemberDiscussionType.PARTICIPATED,
                )
            },
            handleSuccess = { result -> _uiState.update { it.setParticipatedDiscussions(result) } },
            handleFailure = { onUiEvent(MyProfileUiEvent.ShowErrorMessage(it)) },
        )

    private fun loadMyMemberId() {
        viewModelScope.launch {
            _uiState.update { it.setMemberId(tokenRepository.getMemberId()) }
        }
    }

    fun toggleShowMyDiscussion(isShow: Boolean) {
        _uiState.update { it.toggleShowMyDiscussion(isShow) }
    }

    fun modifyProfileImage(imagePayload: ImagePayload) =
        runAsync(
            key = KEY_MODIFY_PROFILE_IMAGE,
            action = { memberRepository.modifyProfileImage(imagePayload) },
            handleSuccess = { result -> _uiState.update { it.modifyProfileImage(result) } },
            handleFailure = { onUiEvent(MyProfileUiEvent.ShowErrorMessage(it)) },
        )

    private fun onUiEvent(event: MyProfileUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_FETCH_PROFILE = "fetch_profile"
        private const val KEY_FETCH_MY_BOOKS = "fetch_my_books"
        private const val KEY_FETCH_LIKED_DISCUSSIONS = "fetch_liked_discussions"
        private const val KEY_FETCH_PARTICIPATED_DISCUSSIONS = "fetch_participated_discussions"
        private const val KEY_MODIFY_PROFILE_IMAGE = "modify_profile_image"
    }
}
