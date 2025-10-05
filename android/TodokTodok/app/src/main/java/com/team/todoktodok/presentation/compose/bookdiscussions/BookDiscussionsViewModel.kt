package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.team.domain.repository.BookRepository
import com.team.domain.repository.DiscussionRepository
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
}
