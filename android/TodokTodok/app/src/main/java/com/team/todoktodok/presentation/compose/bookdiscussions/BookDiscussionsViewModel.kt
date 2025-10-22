package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.team.domain.ConnectivityObserver
import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo
import com.team.domain.model.book.BookDetail
import com.team.domain.repository.BookRepository
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiEvent
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toBookDetailUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toDiscussionItem
import com.team.todoktodok.presentation.core.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
        MutableStateFlow(BookDiscussionsUiState())
    val uiState: StateFlow<BookDiscussionsUiState> = _uiState

    private val _uiEvent = Channel<BookDiscussionsUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

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
            { bookRepository.getBookDetail(bookId) },
            { updateBook(it) },
            { onUiEvent(BookDiscussionsUiEvent.ShowError(it)) },
        )
    }

    fun loadBookDiscussions() {
        runAsync(
            LOAD_BOOK_DISCUSSIONS_KEY,
            { bookRepository.getBookDiscussions(bookId, BOOK_DISCUSSIONS_PAGING_SIZE, null) },
            {
                currentPage = it.pageInfo
                updateBookDiscussionsPage(it.discussions)
            },
            { onUiEvent(BookDiscussionsUiEvent.ShowError(it)) },
        )
    }

    private fun updateBook(book: BookDetail) {
        _uiState.update { currentUiState ->
            currentUiState.copy(book.toBookDetailUiState())
        }
    }

    private fun updateBookDiscussionsPage(discussions: List<Discussion>) {
        _uiState.update { currentUiState ->
            currentUiState.updateBookDiscussions { currentBookDiscussions ->
                currentBookDiscussions.update(discussionItems = discussions.map { it.toDiscussionItem() })
            }
        }
    }

    fun loadMoreItems() {
        val hasNext = currentPage?.hasNext ?: return
        if (_uiState.value.isLoadingBookDiscussions || !hasNext) return
        runAsync(
            LOAD_MORE_BOOK_DISCUSSIONS_KEY,
            {
                bookRepository.getBookDiscussions(
                    bookId,
                    BOOK_DISCUSSIONS_PAGING_SIZE,
                    currentPage?.nextCursor,
                )
            },
            { bookDiscussionsPage ->
                currentPage = bookDiscussionsPage.pageInfo
                addDiscussionItems(bookDiscussionsPage.discussions)
            },
            { onUiEvent(BookDiscussionsUiEvent.ShowError(it)) },
        )
    }

    private fun addDiscussionItems(discussions: List<Discussion>) {
        _uiState.update { uiState ->
            uiState.updateBookDiscussions { bookDiscussionsUiState ->
                bookDiscussionsUiState.addDiscussionItems(discussions.map { it.toDiscussionItem() })
            }
        }
    }

    private fun onUiEvent(event: BookDiscussionsUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    companion object {
        private const val BOOK_DISCUSSIONS_PAGING_SIZE = 2
        private const val LOAD_BOOK_KEY = "LOAD_BOOK_KEY"
        private const val LOAD_BOOK_DISCUSSIONS_KEY = "load_book_discussions_key"
        private const val LOAD_MORE_BOOK_DISCUSSIONS_KEY = "load_more_book_discussions_key"
    }
}
