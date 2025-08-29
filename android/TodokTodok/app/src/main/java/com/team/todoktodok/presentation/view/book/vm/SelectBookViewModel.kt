package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.exception.BookException
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.BookRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.book.SearchedBookResultStatus
import com.team.todoktodok.presentation.view.book.SelectBookUiEvent
import com.team.todoktodok.presentation.view.book.SelectBookUiState
import kotlinx.coroutines.launch

class SelectBookViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<SelectBookUiState> = MutableLiveData(SelectBookUiState())
    val uiState: LiveData<SelectBookUiState> get() = _uiState

    private val _uiEvent: MutableSingleLiveData<SelectBookUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<SelectBookUiEvent> get() = _uiEvent

    fun searchWithCurrentKeyword(keyword: String) {
        val isPossibleSearchKeyword = isPossibleSearchKeyword(keyword)
        updateKeyword(keyword)
        if (isPossibleSearchKeyword) updateSearchedBooks(keyword)
    }

    fun updateKeyword(keyword: String) {
        _uiState.value = _uiState.value?.copy(keyword = keyword)
    }

    private fun isPossibleSearchKeyword(keyword: String): Boolean =
        !(keyword.isBlank() || keyword.isEmpty() || _uiState.value?.isSameKeyword(keyword) == true)

    private fun updateSearchedBooks(keyword: String) {
        _uiState.value = _uiState.value?.copy(status = SearchedBookResultStatus.Loading)
        viewModelScope.launch {
            bookRepository
                .fetchBooks(keyword)
                .onSuccess { books: AladinBooks ->
                    if (books.isEmpty()) {
                        _uiState.value =
                            _uiState.value?.copy(status = SearchedBookResultStatus.NotFound)
                    } else {
                        _uiState.value =
                            _uiState.value?.copy(
                                status = SearchedBookResultStatus.Success,
                                searchedBooks = books,
                            )
                    }
                }.onFailure { exception: TodokTodokExceptions ->
                    _uiState.value =
                        _uiState.value?.copy(status = SearchedBookResultStatus.NotStarted)
                    _uiEvent.setValue(SelectBookUiEvent.ShowException(exception))
                }
        }
    }

    fun updateSelectedBook(position: Int) {
        val selectedBook: AladinBook? = _uiState.value?.selectedBook(position)
        if (selectedBook == null) {
            _uiEvent.setValue(SelectBookUiEvent.ShowException(BookException.EmptySelectedBook))
            return
        }
        _uiEvent.setValue(SelectBookUiEvent.NavigateToCreateDiscussionRoom(selectedBook))
    }
}
