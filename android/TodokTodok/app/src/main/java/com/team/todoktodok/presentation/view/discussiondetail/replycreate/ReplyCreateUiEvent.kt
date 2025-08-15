package com.team.todoktodok.presentation.view.discussiondetail.replycreate

sealed interface ReplyCreateUiEvent {
    data class OnCreateDismiss(
        val content: String,
    ) : ReplyCreateUiEvent

    data object CreateReply : ReplyCreateUiEvent
}
