package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo
import com.team.domain.repository.BookRepository
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiEvent
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState.Empty
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toBookDetailUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toDiscussionItem
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDiscussionsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    connectivityObserver: ConnectivityObserver,
) : BaseViewModel(connectivityObserver) {
    private val route: BookDetailRoute = savedStateHandle.toRoute()
    private val bookId: Long = route.bookId
    private val _uiState: MutableStateFlow<BookDiscussionsUiState> =
        MutableStateFlow(Empty)
    val uiState: StateFlow<BookDiscussionsUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<BookDiscussionsUiEvent>()
    val uiEvent: SharedFlow<BookDiscussionsUiEvent> = _uiEvent

    private var currentPage: PageInfo? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadBook()
        loadBookDiscussions()
    }

    private fun loadBook() {
        runAsync(
            LOAD_BOOK_KEY,
            { bookRepository.fetchBook(bookId) },
            { updateBook(it) },
            { onUiEvent(BookDiscussionsUiEvent.ShowError(it)) },
        )
    }

    private fun loadBookDiscussions() {
        runAsync(
            LOAD_BOOK_DISCUSSIONS_KEY,
            { bookRepository.getBookDiscussions(bookId, 5, null) },
            {
                currentPage = it.pageInfo
                updateBookDiscussionsPage(it.discussions)
            },
            { onUiEvent(BookDiscussionsUiEvent.ShowError(it)) },
        )
    }

    private fun updateBook(book: Book) {
        _uiState.update {
            when (it) {
                Empty -> return
                is BookDiscussionsUiState.Success -> it.copy(bookDetailSectionUiState = book.toBookDetailUiState())
            }
        }
    }

    private fun updateBookDiscussionsPage(discussions: List<Discussion>) {
        _uiState.update { uiState ->
            when (uiState) {
                Empty -> return
                is BookDiscussionsUiState.Success ->
                    uiState.updateBookDiscussions { bookDiscussions ->
                        bookDiscussions.update(
                            discussionItems =
                                discussions
                                    .map { it.toDiscussionItem() }
                                    .toImmutableList(),
                        )
                    }
            }
        }
    }

    fun loadMoreItems() {
        when (val currentUiState = _uiState.value) {
            Empty -> return
            is BookDiscussionsUiState.Success -> {
                val hasNext = currentPage?.hasNext ?: return
                if (currentUiState.isLoadingBookDiscussions || !hasNext) return
                runAsync(
                    LOAD_MORE_BOOK_DISCUSSIONS_KEY,
                    { bookRepository.getBookDiscussions(bookId, 5, currentPage?.nextCursor) },
                    { bookDiscussionsPage ->
                        currentPage = bookDiscussionsPage.pageInfo
                        addDiscussionItems(bookDiscussionsPage.discussions)
                    },
                    { onUiEvent(BookDiscussionsUiEvent.ShowError(it)) },
                )
            }
        }
    }

    private fun addDiscussionItems(discussions: List<Discussion>) {
        _uiState.update { uiState ->
            when (uiState) {
                Empty -> return
                is BookDiscussionsUiState.Success -> {
                    uiState.updateBookDiscussions { bookDiscussionsUiState ->
                        bookDiscussionsUiState.addDiscussionItems(discussions.map { it.toDiscussionItem() })
                    }
                }
            }
        }
    }

    private fun onUiEvent(event: BookDiscussionsUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    companion object {
        private const val LOAD_BOOK_KEY = "LOAD_BOOK_KEY"
        private const val LOAD_BOOK_DISCUSSIONS_KEY = "load_book_discussions_key"
        private const val LOAD_MORE_BOOK_DISCUSSIONS_KEY = "load_more_book_discussions_key"
    }
}
