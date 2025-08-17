package com.team.todoktodok.presentation.view.discussiondetail.replycreate

sealed interface ReplyCreateUiEvent {
    data class SaveContent(
        val content: String,
    ) : ReplyCreateUiEvent

    data object CreateReply : ReplyCreateUiEvent
}
