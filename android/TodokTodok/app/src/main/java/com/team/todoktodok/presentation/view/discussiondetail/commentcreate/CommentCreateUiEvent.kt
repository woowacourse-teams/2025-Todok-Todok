package com.team.todoktodok.presentation.view.discussiondetail.commentcreate

sealed interface CommentCreateUiEvent {
    data object CreateComment : CommentCreateUiEvent
}
