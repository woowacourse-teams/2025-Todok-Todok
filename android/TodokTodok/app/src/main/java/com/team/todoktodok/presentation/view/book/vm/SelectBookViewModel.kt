package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Books
import com.team.domain.repository.BookRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.book.ErrorSelectBookType
import com.team.todoktodok.presentation.view.book.SelectBookUiEvent
import com.team.todoktodok.presentation.view.book.SelectBookUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectBookViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<SelectBookUiState> = MutableLiveData(SelectBookUiState())
    val uiState: LiveData<SelectBookUiState> get() = _uiState

    private val _uiEvent: MutableSingleLiveData<SelectBookUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<SelectBookUiEvent> get() = _uiEvent

    init {
        _uiEvent.setValue(SelectBookUiEvent.RevealKeyboard)
    }

    fun onDeleteKeywordButtonClicked() {
        searchWithCurrentKeyword(NO_KEYWORD)
        _uiEvent.setValue(SelectBookUiEvent.ShowToast(ErrorSelectBookType.ERROR_DELETE_KEYWORD))
    }

    fun onSearchAction(keyword: String) {
        if (keyword.isBlank()) {
            _uiEvent.setValue(SelectBookUiEvent.ShowToast(ErrorSelectBookType.ERROR_EMPTY_KEYWORD))
            return
        }
        _uiEvent.setValue(SelectBookUiEvent.HideKeyboard)
        searchWithCurrentKeyword(keyword)
    }

    private fun searchWithCurrentKeyword(keyword: String) {
        updateKeyword(keyword)
        updateSearchedBooks()
    }

    fun updateSelectedBook(position: Int) {
        _uiState.setValue(_uiState.value?.copy(selectedBook = _uiState.value?.searchedBooks?.get(position)))
        val selectedBook = _uiState.value?.selectedBook
        if (selectedBook == null) {
            _uiEvent.setValue(SelectBookUiEvent.ShowToast(ErrorSelectBookType.ERROR_NO_SELECTED_BOOK))
        } else {
            _uiEvent.setValue(SelectBookUiEvent.NavigateToCreateDiscussionRoom(selectedBook))
        }
    }

    private fun updateKeyword(keyword: String) {
        _uiState.setValue(_uiState.value?.copy(keyword = keyword))
    }

    private fun updateSearchedBooks() {
        val keyword = _uiState.value?.keyword ?: return
        _uiState.value = _uiState.value?.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val books: Books = bookRepository.fetchBooks(keyword)
                _uiState.value = _uiState.value?.copy(isLoading = false, searchedBooks = books)
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(isLoading = false)
                _uiEvent.setValue(SelectBookUiEvent.ShowErrorMessage(SelectBookErrorType.ERROR_NETWORK))
            }
        }
    }

    companion object {
        private const val NO_KEYWORD: String = ""
    }
}
