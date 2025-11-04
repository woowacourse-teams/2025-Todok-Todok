package com.team.todoktodok.presentation.xml.setting.manage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.core.event.MutableSingleLiveData
import com.team.core.event.SingleLiveData
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.xml.setting.manage.ManageBlockedMembersUiEvent
import com.team.todoktodok.presentation.xml.setting.manage.ManageBlockedMembersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBlockedMembersViewModel
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
    ) : ViewModel() {
        private val _uiState = MutableLiveData(ManageBlockedMembersUiState())
        val uiState: LiveData<ManageBlockedMembersUiState> get() = _uiState

        private val _uiEvent = MutableSingleLiveData<ManageBlockedMembersUiEvent>()
        val uiEvent: SingleLiveData<ManageBlockedMembersUiEvent> get() = _uiEvent

        init {
            loadBlockedMembers()
        }

        private fun loadBlockedMembers() {
            withLoading {
                memberRepository
                    .getBlockedMembers()
                    .onSuccess { _uiState.value = _uiState.value?.copy(members = it) }
                    .onFailure { onUiEvent(ManageBlockedMembersUiEvent.ShowErrorMessage(it)) }
            }
        }

        fun onSelectMember(memberId: Long) {
            _uiState.value = _uiState.value?.modifySelectedMember(memberId)
        }

        fun unblockMember() {
            withLoading {
                val currentUiState = _uiState.value ?: throw IllegalArgumentException(NOT_FOUND_MEMBER)
                val memberId = currentUiState.selectedMemberId
                require(memberId != ManageBlockedMembersUiState.NOT_HAS_SELECTED_MEMBER) { NOT_FOUND_MEMBER }

                memberRepository
                    .unblock(memberId)
                    .onSuccess { _uiState.value = currentUiState.removeSelectedMember() }
                    .onFailure { onUiEvent(ManageBlockedMembersUiEvent.ShowErrorMessage(it)) }
            }
        }

        private fun onUiEvent(event: ManageBlockedMembersUiEvent) {
            _uiEvent.setValue(event)
        }

        private fun withLoading(action: suspend () -> Unit) {
            viewModelScope.launch {
                changeLoading()
                action()
                changeLoading()
            }
        }

        private fun changeLoading() {
            _uiState.value = _uiState.value?.toggleLoading()
        }

        companion object {
            private const val NOT_FOUND_MEMBER = "선택된 유저가 없습니다."
        }
    }
