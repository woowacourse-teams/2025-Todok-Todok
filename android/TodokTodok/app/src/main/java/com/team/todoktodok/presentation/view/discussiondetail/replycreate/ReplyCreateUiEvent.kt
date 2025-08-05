package com.team.todoktodok.presentation.view.discussiondetail.replycreate

sealed interface ReplyCreateUiEvent {
    data object CreateReply : ReplyCreateUiEvent
}
