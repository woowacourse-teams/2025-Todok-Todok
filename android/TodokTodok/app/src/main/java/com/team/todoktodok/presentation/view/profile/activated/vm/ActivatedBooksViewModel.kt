package com.team.todoktodok.presentation.view.profile.activated.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.model.member.MemberId.Companion.MemberId
import com.team.domain.repository.MemberRepository
import kotlinx.coroutines.launch

class ActivatedBooksViewModel(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    fun loadActivatedBooks(id: Long) {
        viewModelScope.launch {
            val memberId = MemberId(id)
            _books.value = memberRepository.getMemberBooks(memberId)
        }
    }
}
