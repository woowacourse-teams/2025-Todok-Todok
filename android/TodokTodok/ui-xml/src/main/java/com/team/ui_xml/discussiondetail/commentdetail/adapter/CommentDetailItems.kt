package com.team.ui_xml.discussiondetail.commentdetail.adapter

import com.team.ui_xml.discussiondetail.model.CommentItemUiState
import com.team.ui_xml.discussiondetail.model.ReplyItemUiState

sealed class CommentDetailItems(
    val viewType: ViewType,
) {
    data class CommentItem(
        val value: CommentItemUiState,
    ) : CommentDetailItems(ViewType.COMMENT_ITEM)

    data class ReplyItem(
        val value: ReplyItemUiState,
    ) : CommentDetailItems(ViewType.REPLIES_ITEM)

    enum class ViewType(
        val sequence: Int,
    ) {
        COMMENT_ITEM(0),
        REPLIES_ITEM(1),
        ;

        companion object {
            fun ViewType(sequence: Int): ViewType =
                entries.find { it.sequence == sequence }
                    ?: throw IllegalArgumentException(INVALID_INDEX_VIEW_TYPE.format(sequence))

            private const val INVALID_INDEX_VIEW_TYPE = "뷰타입의 순서가 잘못 되었습니다. [%d]"
        }
    }
}
