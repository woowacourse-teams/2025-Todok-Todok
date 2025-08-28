package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Books
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.book.SelectBookErrorType
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
        val isPossibleSearchKeyword = checkKeyword(keyword)
        updateKeyword(keyword)
        if (isPossibleSearchKeyword) updateSearchedBooks()
    }

    fun updateSelectedBook(position: Int) {
        if (_uiState.value?.isExist(position) == false) {
            _uiEvent.setValue(SelectBookUiEvent.ShowError(SelectBookErrorType.ERROR_NO_SELECTED_BOOK))
            return
        }
        val selectedBook =
            _uiState.value?.searchedBooks?.get(position) ?: run {
                _uiEvent.setValue(SelectBookUiEvent.ShowError(SelectBookErrorType.ERROR_NO_SELECTED_BOOK))
                return
            }
        _uiState.value = _uiState.value?.copy(selectedBook = selectedBook)
        _uiEvent.setValue(SelectBookUiEvent.NavigateToCreateDiscussionRoom(selectedBook))
    }

    fun updateKeyword(keyword: String) {
        _uiState.value = _uiState.value?.copy(keyword = keyword)
    }

    private fun checkKeyword(keyword: String): Boolean {
        if (keyword.isBlank()) {
            // api 호출이 안됨
            return false
        }
        if (keyword == _uiState.value?.keyword) {
            // api 호출이 안됨
            return false
        }
        return true
    }

    private fun updateSearchedBooks() {
        val keyword = _uiState.value?.keyword ?: return
        _uiState.value = _uiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            bookRepository
                .fetchBooks(keyword)
                .onSuccess { books: Books ->
                    _uiState.value = _uiState.value?.copy(isLoading = false, searchedBooks = books)
                    if (books.size == SEARCHED_BOOKS_IS_EMPTY) {

                    }
                }.onFailure { exception: TodokTodokExceptions ->
                    _uiState.value = _uiState.value?.copy(isLoading = false)
                    _uiEvent.setValue(SelectBookUiEvent.ShowTodokTodokException(exception))
                }
        }
    }

    companion object {
        private const val SEARCHED_BOOKS_IS_EMPTY: Int = 0
    }
}
