package com.team.todoktodok.presentation.xml.discussions.my.adapter

import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

sealed class MyDiscussionItems(
    val viewType: ViewType,
) {
    data class CreatedDiscussionItem(
        val items: List<DiscussionUiState>,
    ) : MyDiscussionItems(ViewType.CREATED_DISCUSSIONS)

    data object DividerItem : MyDiscussionItems(ViewType.DIVIDER)

    data class ParticipatedDiscussionItem(
        val items: List<DiscussionUiState>,
    ) : MyDiscussionItems(ViewType.PARTICIPATED_DISCUSSIONS)

    enum class ViewType(
        val sequence: Int,
    ) {
        CREATED_DISCUSSIONS(0),
        DIVIDER(1),
        PARTICIPATED_DISCUSSIONS(2),
        ;

        companion object {
            fun ViewType(index: Int): ViewType =
                when (index) {
                    0 -> CREATED_DISCUSSIONS
                    1 -> DIVIDER
                    2 -> PARTICIPATED_DISCUSSIONS
                    else -> throw IllegalArgumentException(INVALID_INDEX_VIEW_TYPE.format(index))
                }

            private const val INVALID_INDEX_VIEW_TYPE = "뷰타입의 인덱스가 잘못 되었습니다. [%d]"
        }
    }
}
