package com.team.todoktodok.presentation.utview.creatediscussionroom.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.model.Book
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.utview.creatediscussionroom.CreateDiscussionRoomUiEvent
import com.team.todoktodok.presentation.utview.creatediscussionroom.CreateDiscussionRoomUiState
import com.team.todoktodok.presentation.view.serialization.SerializationBook
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
            _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(ERROR_EMPTY_SEARCH_INPUT)
            return
        }

        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            val books = bookRepository.searchBooks(input)
            if (books.isEmpty()) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(ERROR_NO_SEARCH_RESULTS)
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
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(ERROR_NO_SELECTED_BOOK)
                return@launch
            }
            if (discussionTitle == null) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(ERROR_BOOK_NOT_FOUND)
                return@launch
            }
            if (discussionContent == null) {
                _uiEvent.value = CreateDiscussionRoomUiEvent.ShowDialog(ERROR_BOOK_NOT_FOUND)
                return@launch
            }
            discussionRepository.saveDiscussion(
                selectedBook.id,
                discussionTitle,
                discussionContent,
            )
            _uiEvent.value =
                CreateDiscussionRoomUiEvent.CreateDiscussionRoom(
                    discussionTitle,
                    discussionContent,
                    selectedBook,
                )
        }
    }

    fun moveToBack() {
        _uiEvent.value = CreateDiscussionRoomUiEvent.NavigateToBack
    }

    fun clearUiState() {
        _uiEvent.value = null
    }

    companion object {
        private const val MESSAGE_BOOK_SAVED: String = "책이 저장되었습니다"
        private const val ERROR_NO_SELECTED_BOOK: String = "선택된 책이 없습니다"
        private const val ERROR_BOOK_NOT_FOUND: String = "책을 찾을 수 없습니다"
        private const val ERROR_NO_SEARCH_RESULTS: String = "검색 결과가 없습니다"
        private const val ERROR_EMPTY_SEARCH_INPUT: String = "검색어를 입력해주세요"
    }
}
