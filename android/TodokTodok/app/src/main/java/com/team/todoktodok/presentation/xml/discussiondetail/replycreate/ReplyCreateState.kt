package com.team.todoktodok.presentation.xml.discussiondetail.replycreate

sealed interface ReplyCreateState {
    data object Create : ReplyCreateState

    data class Update(
        val replyId: Long,
    ) : ReplyCreateState

    companion object {
        fun create(replyId: Long?) =
            replyId?.let { Update(it) }
                ?: Create
    }
}
