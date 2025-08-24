package com.team.todoktodok.presentation.view.discussions.hot

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionItems
import com.team.todoktodok.presentation.view.discussions.toUiState

data class HotDiscussionUiState(
    val items: List<HotDiscussionItems> = emptyList(),
    val activatedPage: PageInfo = PageInfo.EMPTY,
) {
    fun add(
        hotDiscussions: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): HotDiscussionUiState {
        val popularItems = hotDiscussions.map { DiscussionUiState(it, true) }
        val activatedItems = activatedDiscussion.data.map { it.toUiState() }

        val hotDiscussion =
            listOf(
                HotDiscussionItems.PopularItem(popularItems),
                HotDiscussionItems.ActivatedHeaderItem,
                HotDiscussionItems.ActivatedItem(activatedItems),
            )

        return copy(
            items = hotDiscussion,
            activatedPage = activatedDiscussion.pageInfo,
        )
    }

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): HotDiscussionUiState {
        val activatedIndex = HotDiscussionItems.ViewType.ACTIVATED.sequence
        val activatedItem =
            items.getOrNull(activatedIndex) as? HotDiscussionItems.ActivatedItem
                ?: return this

        val updatedItems = activatedItem.items + page.data.map { it.toUiState() }

        val updatedList =
            items.toMutableList().apply {
                this[activatedIndex] = HotDiscussionItems.ActivatedItem(updatedItems)
            }

        return copy(updatedList, page.pageInfo)
    }
}
