package com.team.todoktodok.presentation.view.setting.manage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.member.BlockedMember
import com.team.domain.repository.MemberRepository
import kotlinx.coroutines.launch

class ManageBlockedMembersViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _blockedMembers = MutableLiveData<List<BlockedMember>>()
    val blockedMembers: LiveData<List<BlockedMember>> get() = _blockedMembers

    private var selectedMember: BlockedMember? = null

    init {
        loadBlockedMembers()
    }

    private fun loadBlockedMembers() {
        viewModelScope.launch {
            _blockedMembers.value = memberRepository.getBlockedMembers()
        }
    }

    fun findMember(index: Int) {
        selectedMember = _blockedMembers.value?.get(index)
    }

    fun unblockMember() {
        viewModelScope.launch {
            val memberId = selectedMember?.memberId
            requireNotNull(memberId) { NOT_FOUND_MEMBER }

            _blockedMembers.value = _blockedMembers.value?.filter { it.memberId != memberId }
            memberRepository.unblock(memberId)
        }
    }

    companion object {
        private const val NOT_FOUND_MEMBER = "선택된 유저가 없습니다."
    }
}
