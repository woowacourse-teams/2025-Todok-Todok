package com.team.todoktodok.presentation.xml.discussions.hot.adapter

import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

sealed class HotDiscussionItems(
    val viewType: ViewType,
) {
    data class PopularItem(
        val items: List<DiscussionUiState>,
    ) : HotDiscussionItems(ViewType.POPULAR)

    data object ActivatedHeaderItem : HotDiscussionItems(ViewType.ACTIVATED_HEADER)

    data class ActivatedItem(
        val items: List<DiscussionUiState>,
    ) : HotDiscussionItems(ViewType.ACTIVATED)

    enum class ViewType(
        val sequence: Int,
    ) {
        POPULAR(0),
        ACTIVATED_HEADER(1),
        ACTIVATED(2),
        ;

        companion object {
            fun ViewType(index: Int): ViewType =
                ViewType.entries.find { it.sequence == index }
                    ?: throw IllegalArgumentException(INVALID_INDEX_VIEW_TYPE.format(index))

            private const val INVALID_INDEX_VIEW_TYPE = "뷰타입의 인덱스가 잘못 되었습니다. [%d]"
        }
    }
}
