package com.team.todoktodok.presentation.view.setting.modify.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository

class ModifyProfileViewModelFactory(
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyProfileViewModel::class.java)) {
            return ModifyProfileViewModel(memberRepository) as T
        }
        throw IllegalArgumentException()
    }
}
