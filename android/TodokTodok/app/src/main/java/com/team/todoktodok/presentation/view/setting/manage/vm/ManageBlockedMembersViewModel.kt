package com.team.todoktodok.presentation.view.setting.manage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.view.setting.manage.ManageBlockedMembersUiState
import kotlinx.coroutines.launch

class ManageBlockedMembersViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(ManageBlockedMembersUiState())
    val uiState: LiveData<ManageBlockedMembersUiState> get() = _uiState

    init {
        loadBlockedMembers()
    }

    private fun loadBlockedMembers() {
        viewModelScope.launch {
            val result = memberRepository.getBlockedMembers()
            _uiState.value = _uiState.value?.copy(members = result)
        }
    }

    fun onSelectMember(memberId: Long) {
        _uiState.value = _uiState.value?.modifySelectedMember(memberId)
    }

    fun unblockMember() {
        viewModelScope.launch {
            val currentUiState = _uiState.value ?: throw IllegalArgumentException(NOT_FOUND_MEMBER)
            val memberId = currentUiState.selectedMemberId
            require(memberId != -1L) { NOT_FOUND_MEMBER }

            _uiState.value = currentUiState.removeSelectedMember()
            memberRepository.unblock(memberId)
        }
    }

    companion object {
        private const val NOT_FOUND_MEMBER = "선택된 유저가 없습니다."
    }
}
