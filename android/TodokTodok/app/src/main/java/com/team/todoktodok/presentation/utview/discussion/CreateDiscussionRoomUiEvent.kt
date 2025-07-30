package com.team.todoktodok.presentation.utview.discussion

import com.team.domain.model.Book
import com.team.todoktodok.presentation.utview.discussion.vm.SearchBookErrorType
import com.team.todoktodok.presentation.view.serialization.SerializationBook

sealed interface CreateDiscussionRoomUiEvent {
    data object NavigateToBack : CreateDiscussionRoomUiEvent

    data class ShowSearchedBooks(
        val books: List<SerializationBook>,
    ) : CreateDiscussionRoomUiEvent

    data class ShowSelectedBook(
        val book: Book,
    ) : CreateDiscussionRoomUiEvent

    data class ShowDialog(
        val errorType: SearchBookErrorType,
    ) : CreateDiscussionRoomUiEvent

    data class CreateDiscussionRoom(
        val title: String,
        val content: String,
        val selectedBook: Book,
    ) : CreateDiscussionRoomUiEvent
}
