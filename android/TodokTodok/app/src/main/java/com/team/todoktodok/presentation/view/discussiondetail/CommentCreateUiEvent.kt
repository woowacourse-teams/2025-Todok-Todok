package com.team.todoktodok.presentation.view.discussiondetail

sealed interface CommentCreateUiEvent {
    data object CreateComment : CommentCreateUiEvent
}
