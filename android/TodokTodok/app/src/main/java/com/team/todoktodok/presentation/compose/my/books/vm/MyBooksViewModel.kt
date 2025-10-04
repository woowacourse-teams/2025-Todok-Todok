package com.team.todoktodok.presentation.compose.my.books.vm

import androidx.lifecycle.viewModelScope
import com.team.domain.ConnectivityObserver
import com.team.domain.model.member.MemberId
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiEvent
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiState
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MyBooksViewModel(
    private val memberRepository: MemberRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val _uiState = MutableStateFlow(MyBooksUiState())
    val uiState: StateFlow<MyBooksUiState> get() = _uiState.asStateFlow()

    private val _uiEvent = Channel<MyBooksUiEvent>(Channel.BUFFERED)
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun loadMyBooks() =
        runAsync(
            key = KEY_FETCH_MY_BOOKS,
            action = { memberRepository.getMemberBooks(MemberId.Mine) },
            handleSuccess = { _uiState.value = _uiState.value.copy(books = it) },
            handleFailure = { onUiEvent(MyBooksUiEvent.ShowErrorMessage(it)) },
        )

    private fun onUiEvent(event: MyBooksUiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private const val KEY_FETCH_MY_BOOKS = "fetch_my_books"
    }
}
