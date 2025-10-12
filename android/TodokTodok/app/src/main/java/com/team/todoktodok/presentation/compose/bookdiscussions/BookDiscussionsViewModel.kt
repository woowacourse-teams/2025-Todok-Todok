package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.team.domain.model.PageInfo
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsSectionUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiEvent
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toBookDetailUiState
import com.team.todoktodok.presentation.compose.bookdiscussions.model.toDiscussionItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookDiscussionsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val discussionRepository: DiscussionRepository,
) : ViewModel() {
    private val route: BookDetailRoute = savedStateHandle.toRoute()
    private val bookId: Long = route.bookId
    private val _uiState: MutableStateFlow<BookDiscussionsUiState> =
        MutableStateFlow(BookDiscussionsUiState.Loading)
    val uiState: StateFlow<BookDiscussionsUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<BookDiscussionsUiEvent>()
    val uiEvent: SharedFlow<BookDiscussionsUiEvent> = _uiEvent

    private var currentPage: PageInfo? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val bookResultDeferred = async { bookRepository.fetchBook(bookId) }
            val discussionsResultDeferred =
                async { discussionRepository.getBookDiscussions(bookId, 20, null) }

            val bookResult = bookResultDeferred.await()
            val discussionsResult = discussionsResultDeferred.await()

            val book =
                when (bookResult) {
                    is NetworkResult.Success -> bookResult.data
                    is NetworkResult.Failure -> {
                        _uiState.value = BookDiscussionsUiState.Failure(bookResult.exception)
                        onUiEvent(BookDiscussionsUiEvent.ShowErrorEvent(bookResult.exception))
                        return@launch
                    }
                }

            val discussionsPage =
                when (discussionsResult) {
                    is NetworkResult.Success -> discussionsResult.data
                    is NetworkResult.Failure -> {
                        _uiState.value = BookDiscussionsUiState.Failure(discussionsResult.exception)
                        onUiEvent(BookDiscussionsUiEvent.ShowErrorEvent(discussionsResult.exception))
                        return@launch
                    }
                }

            currentPage = discussionsPage.pageInfo
            _uiState.value =
                BookDiscussionsUiState.Success(
                    book = book.toBookDetailUiState(),
                    bookDiscussionsSectionUiState =
                        BookDiscussionsSectionUiState(
                            isPagingLoading = false,
                            discussionItems =
                                discussionsPage.discussions
                                    .map { it.toDiscussionItem() }
                                    .toImmutableList(),
                        ),
                )
        }
    }

    fun loadMoreItems() {
        val currentSuccessState = _uiState.value as? BookDiscussionsUiState.Success ?: return

        val isPagingLoading = currentSuccessState.bookDiscussionsSectionUiState.isPagingLoading
        val hasNext = currentPage?.hasNext ?: return
        if (isPagingLoading || !hasNext) return

        viewModelScope.launch {
            _uiState.value =
                currentSuccessState.copy(
                    bookDiscussionsSectionUiState =
                        currentSuccessState.bookDiscussionsSectionUiState.copy(
                            isPagingLoading = true,
                        ),
                )

            when (
                val result =
                    discussionRepository.getBookDiscussions(bookId, 20, currentPage?.nextCursor)
            ) {
                is NetworkResult.Success -> {
                    val newDiscussionsPage = result.data
                    val currentItems =
                        currentSuccessState.bookDiscussionsSectionUiState.discussionItems
                    val newItems =
                        currentItems + newDiscussionsPage.discussions.map { it.toDiscussionItem() }

                    currentPage = newDiscussionsPage.pageInfo
                    _uiState.value =
                        currentSuccessState.copy(
                            bookDiscussionsSectionUiState =
                                BookDiscussionsSectionUiState(
                                    isPagingLoading = false,
                                    discussionItems = newItems.toImmutableList(),
                                ),
                        )
                }

                is NetworkResult.Failure -> {
                    onUiEvent(BookDiscussionsUiEvent.ShowErrorEvent(result.exception))
                    _uiState.value =
                        currentSuccessState.copy(
                            bookDiscussionsSectionUiState =
                                currentSuccessState.bookDiscussionsSectionUiState.copy(
                                    isPagingLoading = false,
                                ),
                        )
                }
            }
        }
    }

    private fun onUiEvent(event: BookDiscussionsUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
