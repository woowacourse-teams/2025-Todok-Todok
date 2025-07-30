package com.team.todoktodok.presentation.utview.discussion.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.utview.discussion.CreateDiscussionRoomUiEvent
import com.team.todoktodok.presentation.utview.discussion.CreateDiscussionRoomUiState
import com.team.todoktodok.presentation.view.serialization.SerializationBook
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreateDiscussionRoomViewModel(
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private var uiState: CreateDiscussionRoomUiState = CreateDiscussionRoomUiState()
    private val _uiEvent: MutableLiveData<CreateDiscussionRoomUiEvent> = MutableLiveData()
    val uiEvent: LiveData<CreateDiscussionRoomUiEvent> get() = _uiEvent

    fun updateSearchInput(searchInput: String) {
        val currentState = uiState
        val updatedState = currentState.copy(searchInput = searchInput)
        uiState = updatedState
    }

    fun updateDiscussionTitle(discussionTitle: String) {
        val currentState = uiState
        val updatedState = currentState.copy(discussionTitle = discussionTitle)
        uiState = updatedState
    }

    fun updateDiscussionContent(discussionContent: String) {
        val currentState = uiState
        val updatedState = currentState.copy(discussionContent = discussionContent)
        uiState = updatedState
    }

    fun searchBooks() {
        val input = uiState.searchInput.orEmpty()
        if (input.isEmpty()) {
            _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(SearchBookErrorType.EMPTY_SEARCH_INPUT)
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val books = bookRepository.searchBooks(input)
            if (books.isEmpty()) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(SearchBookErrorType.NO_SEARCH_RESULTS)
                return@launch
            }
            val serializationBooks =
                books.map { book ->
                    SerializationBook(
                        book.id,
                        book.title,
                        book.author,
                        book.image,
                    )
                }
            uiState = uiState.copy(isLoading = false, searchedBooks = books)
            _uiEvent.value = CreateDiscussionRoomUiEvent.ShowSearchedBooks(serializationBooks)
        }
    }

    fun updateSelectedBook(selectedPosition: Int) {
        val selectedBook: Book = uiState.searchedBooks[selectedPosition]
        val currentState = uiState
        val updatedState = currentState.copy(selectedBook = selectedBook)
        uiState = updatedState
        _uiEvent.value = CreateDiscussionRoomUiEvent.ShowSelectedBook(selectedBook)
    }

    fun saveDiscussionRoom() {
        val selectedBook = uiState.selectedBook
        val discussionTitle = uiState.discussionTitle
        val discussionContent = uiState.discussionContent
        viewModelScope.launch {
            if (selectedBook == null) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(SearchBookErrorType.NO_SELECTED_BOOK)
                return@launch
            }
            if (discussionTitle == null) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(SearchBookErrorType.BOOK_NOT_FOUND)
                return@launch
            }
            if (discussionContent == null) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(SearchBookErrorType.BOOK_NOT_FOUND)
                return@launch
            }
            val saveBookId: Deferred<Long?> =
                async { bookRepository.saveSelectedBook(selectedBook) }
            val bookId = saveBookId.await() ?: -1
            discussionRepository.saveDiscussionRoom(
                bookId,
                discussionTitle,
                discussionContent,
            )
            _uiEvent.value =
                CreateDiscussionRoomUiEvent.NavigateToBack
        }
    }

    fun moveToBack() {
        _uiEvent.value = CreateDiscussionRoomUiEvent.NavigateToBack
    }

    fun clearUiState() {
        _uiEvent.value = null
    }
}

