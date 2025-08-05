package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Books
import com.team.domain.repository.BookRepository
import com.team.todoktodok.R
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.book.SelectBookUiEvent
import com.team.todoktodok.presentation.view.book.SelectBookUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectBookViewModel(
    private val bookRepository: BookRepository,
) : ViewModel() {
    private var uiState = SelectBookUiState()

    private val _uiEvent: MutableSingleLiveData<SelectBookUiEvent> = MutableSingleLiveData()
    val uiEvent: SingleLiveData<SelectBookUiEvent> get() = _uiEvent

    init {
        isLoading()
        _uiEvent.setValue(SelectBookUiEvent.RevealKeyboard)
    }

    fun onDeleteKeywordButtonClicked() {
        searchWithCurrentKeyword(NO_KEYWORD)
        _uiEvent.setValue(SelectBookUiEvent.ShowDialog(R.string.error_delete_keyword))
    }

    fun onSearchAction(keyword: String) {
        if (keyword.isBlank()) {
            _uiEvent.setValue(SelectBookUiEvent.ShowDialog(R.string.error_empty_keyword))
            return
        }
        _uiEvent.setValue(SelectBookUiEvent.HideKeyboard)
        searchWithCurrentKeyword(keyword)
    }

    private fun searchWithCurrentKeyword(keyword: String) {
        updateKeyword(keyword)
        updateSearchedBooks()
    }

    fun navigateToBack() {
        _uiEvent.setValue(SelectBookUiEvent.NavigateToBack)
    }

    fun updateSelectedBook(position: Int) {
        uiState = uiState.copy(selectedBook = uiState.searchedBooks[position])
        val selectedBook = uiState.selectedBook
        if (selectedBook == null) {
            _uiEvent.setValue(SelectBookUiEvent.ShowDialog(R.string.error_no_selected_book))
        } else {
            _uiEvent.setValue(SelectBookUiEvent.NavigateToCreateDiscussionRoom(selectedBook))
        }
    }

    private fun updateKeyword(keyword: String) {
        uiState = uiState.copy(keyword = keyword)
    }

    private fun updateSearchedBooks() {
        val keyword = uiState.keyword
        uiState = uiState.copy(isLoading = true)
        isLoading()
        viewModelScope.launch {
            try {
                val books: Books =
                    withContext(Dispatchers.IO) {
                        bookRepository.getBooks(keyword)
                    }
                uiState = uiState.copy(isLoading = false, searchedBooks = books)
                isLoading()
                _uiEvent.setValue(SelectBookUiEvent.ShowSearchResult(uiState.searchedBooks))
            } catch (e: Exception) {
                _uiEvent.setValue(SelectBookUiEvent.ShowDialog(R.string.error_network))
            }
        }
    }

    private fun isLoading() {
        if (uiState.isLoading) {
            _uiEvent.setValue(SelectBookUiEvent.StartLoading)
            return
        }
        _uiEvent.setValue(SelectBookUiEvent.FinishLoading)
    }

    companion object {
        private const val NO_KEYWORD: String = ""
    }
}
