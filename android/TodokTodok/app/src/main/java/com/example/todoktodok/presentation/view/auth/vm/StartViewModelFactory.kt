package com.example.todoktodok.presentation.view.auth.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.MemberRepository

class StartViewModelFactory(
    private val memberRepository: MemberRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            return StartViewModel(memberRepository) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
