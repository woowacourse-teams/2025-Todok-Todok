package com.team.todoktodok.presentation.view.book.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.SearchedBook
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

    fun changePageSize(size: Int) {
        setState { copy(pageSize = size) }
    }

    fun searchWithCurrentKeyword(keyword: String) {
        val isPossibleSearchKeyword = isPossibleSearchKeyword(keyword)
        updateKeyword(keyword)
        if (isPossibleSearchKeyword) updateSearchedBooks(keyword)
    }

    fun updateSelectedBook(position: Int) {
        val selectedBook: SearchedBook? = _uiState.value?.selectedBook(position)
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

    fun isNotPossibleAddSearchedBooks(): Boolean =
        !(_uiState.value?.hasNextPage ?: false) ||
            _uiState.value?.status == SearchedBookStatus.Loading

    fun addSearchedBooks() {
        if (isNotPossibleAddSearchedBooks()) return

        setState { copy(status = SearchedBookStatus.Loading) }
        viewModelScope.launch {
            _uiState.value?.keyword?.let { keyword ->
                getSearchedBooksMore(keyword)
            }
        }
    }

    private fun updateSearchedBooks(value: String) {
        setState { copy(status = SearchedBookStatus.Loading, keyword = Keyword(value)) }
        viewModelScope.launch {
            _uiState.value?.keyword?.let { keyword ->
                getSearchedBooksFirst(keyword)
            }
        }
    }

    private fun isPossibleSearchKeyword(keyword: String): Boolean =
        !(keyword.isBlank() || keyword.isEmpty() || _uiState.value?.isSameKeyword(keyword) == true)

    private suspend fun getSearchedBooksFirst(keyword: Keyword) {
        bookRepository
            .fetchBooks(size = _uiState.value?.pageSize ?: 1, keyword = keyword)
            .onSuccess { searchedBooksResult ->
                if (searchedBooksResult.books.isEmpty()) {
                    setState { copy(status = SearchedBookStatus.NotFound) }
                    return@onSuccess
                }
                setState {
                    copy(
                        status = SearchedBookStatus.Success,
                        searchedBooksResult = searchedBooksResult,
                    )
                }
            }.onFailure { exception: TodokTodokExceptions ->
                setState { copy(status = SearchedBookStatus.NotStarted) }
                _uiEvent.setValue(SelectBookUiEvent.ShowException(exception))
            }
    }

    private suspend fun getSearchedBooksMore(keyword: Keyword) {
        bookRepository
            .fetchBooks(size = _uiState.value?.pageSize ?: 1, keyword)
            .onSuccess { searchedBooksResult ->
                setState {
                    copy(
                        status = SearchedBookStatus.Success,
                        searchedBooksResult =
                            _uiState.value?.searchedBooksResult?.addSearchedBooks(
                                searchedBooksResult.books,
                                searchedBooksResult.hasNext,
                            ),
                    )
                }
            }.onFailure { exception ->
                setState { copy(status = SearchedBookStatus.NotStarted) }
                _uiEvent.setValue(SelectBookUiEvent.ShowException(exception))
            }
    }

    private inline fun setState(transform: SelectBookUiState.() -> SelectBookUiState) {
        _uiState.value = _uiState.value?.transform()
    }
}
