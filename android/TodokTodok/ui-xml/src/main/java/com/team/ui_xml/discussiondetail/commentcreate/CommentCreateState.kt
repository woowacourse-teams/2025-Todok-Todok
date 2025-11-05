package com.team.ui_xml.discussiondetail.commentcreate

sealed interface CommentCreateState {
    data object Create : CommentCreateState

    data class Update(
        val commentId: Long,
    ) : CommentCreateState

    companion object {
        fun create(commentId: Long?) =
            commentId?.let { Update(it) }
                ?: Create
    }
}
