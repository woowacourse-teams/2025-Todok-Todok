package com.team.todoktodok.presentation.utview.discussiondetail

sealed interface CommentCreateUiEvent {
    data object CreateComment : CommentCreateUiEvent
}
