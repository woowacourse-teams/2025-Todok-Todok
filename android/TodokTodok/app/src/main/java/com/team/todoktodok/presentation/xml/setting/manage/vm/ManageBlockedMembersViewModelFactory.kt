package com.team.todoktodok.presentation.xml.setting.manage.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository

class ManageBlockedMembersViewModelFactory(
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageBlockedMembersViewModel::class.java)) {
            return ManageBlockedMembersViewModel(memberRepository) as T
        }
        throw IllegalArgumentException()
    }
}
