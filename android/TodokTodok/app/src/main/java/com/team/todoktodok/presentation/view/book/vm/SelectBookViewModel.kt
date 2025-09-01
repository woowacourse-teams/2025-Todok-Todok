package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.AladinBooks
import com.team.domain.model.book.Keyword
import com.team.domain.model.exception.BookException
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import com.team.domain.repository.BookRepository
import com.team.todoktodok.presentation.core.event.MutableSingleLiveData
import com.team.todoktodok.presentation.core.event.SingleLiveData
import com.team.todoktodok.presentation.view.book.SearchedBookStatus
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

    fun updateSelectedBook(position: Int) {
        val selectedBook: AladinBook? = _uiState.value?.selectedBook(position)
        if (selectedBook == null) {
            _uiEvent.setValue(SelectBookUiEvent.ShowException(BookException.EmptySelectedBook))
            return
        }
        _uiEvent.setValue(SelectBookUiEvent.NavigateToCreateDiscussionRoom(selectedBook))
    }

    fun updateKeyword(value: String) {
        if (value.isBlank() || value.isEmpty()) {
            setState { copy(keyword = null) }
            return
        }
        setState { copy(keyword = Keyword(value)) }
    }

    private fun isPossibleSearchKeyword(keyword: String): Boolean =
        !(keyword.isBlank() || keyword.isEmpty() || _uiState.value?.isSameKeyword(keyword) == true)

    private fun updateSearchedBooks(value: String) {
        setState { copy(status = SearchedBookStatus.Loading, keyword = Keyword(value)) }
        viewModelScope.launch {
            _uiState.value?.keyword?.let {
                bookRepository
                    .fetchBooks(it)
                    .onSuccess { books: AladinBooks ->
                        if (books.isEmpty()) {
                            setState { copy(status = SearchedBookStatus.NotFound) }
                            return@onSuccess
                        }
                        setState {
                            copy(
                                status = SearchedBookStatus.Success,
                                searchedBooks = books,
                            )
                        }
                    }.onFailure { exception: TodokTodokExceptions ->
                        setState { copy(status = SearchedBookStatus.NotStarted) }
                        _uiEvent.setValue(SelectBookUiEvent.ShowException(exception))
                    }
            }
        }
    }

    private inline fun setState(transform: SelectBookUiState.() -> SelectBookUiState) {
        _uiState.value = _uiState.value?.transform()
    }
}
