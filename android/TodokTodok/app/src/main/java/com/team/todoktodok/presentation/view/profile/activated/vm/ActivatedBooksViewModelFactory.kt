package com.team.todoktodok.presentation.view.profile.activated.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team.domain.repository.MemberRepository

class ActivatedBooksViewModelFactory(
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivatedBooksViewModel::class.java)) {
            return ActivatedBooksViewModel(memberRepository) as T
        }
        throw IllegalArgumentException()
    }
}
