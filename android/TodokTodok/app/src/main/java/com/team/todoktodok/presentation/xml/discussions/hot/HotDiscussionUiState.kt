package com.team.todoktodok.presentation.xml.discussions.hot

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState
import com.team.todoktodok.presentation.xml.discussions.hot.adapter.HotDiscussionItems
import com.team.todoktodok.presentation.xml.discussions.toUiState

data class HotDiscussionUiState(
    val items: List<HotDiscussionItems> = emptyList(),
    val activatedPage: PageInfo = PageInfo.EMPTY,
) {
    fun add(
        hotDiscussions: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): HotDiscussionUiState {
        val popularItems = hotDiscussions.map { DiscussionUiState(it) }
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

    fun removeDiscussion(discussionId: Long): HotDiscussionUiState {
        val newPopularItems = removePopularDiscussion(discussionId)
        val newActivatedItems = removeActivatedDiscussion(discussionId)
        val hotDiscussion =
            listOf(newPopularItems, HotDiscussionItems.ActivatedHeaderItem, newActivatedItems)

        return copy(items = hotDiscussion)
    }

    private fun removePopularDiscussion(discussionId: Long): HotDiscussionItems.PopularItem {
        val newItems =
            items
                .filterIsInstance<HotDiscussionItems.PopularItem>()
                .firstOrNull()
                ?.items
                ?.filter { it.discussionId != discussionId } ?: emptyList()
        return HotDiscussionItems.PopularItem(newItems)
    }

    private fun removeActivatedDiscussion(discussionId: Long): HotDiscussionItems.ActivatedItem {
        val newItems =
            items
                .filterIsInstance<HotDiscussionItems.ActivatedItem>()
                .firstOrNull()
                ?.items
                ?.filter { it.discussionId != discussionId } ?: emptyList()
        return HotDiscussionItems.ActivatedItem(newItems)
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

    fun modifyDiscussion(discussion: Discussion): HotDiscussionUiState {
        val newPopularItems = modifyPopularDiscussion(discussion)
        val newActivatedItems = modifyActivatedDiscussion(discussion)
        val hotDiscussion =
            listOf(newPopularItems, HotDiscussionItems.ActivatedHeaderItem, newActivatedItems)
        return copy(items = hotDiscussion)
    }

    private fun modifyActivatedDiscussion(discussion: Discussion): HotDiscussionItems.ActivatedItem {
        val newItems =
            items
                .filterIsInstance<HotDiscussionItems.ActivatedItem>()
                .firstOrNull()
                ?.items
                ?.map {
                    if (it.discussionId == discussion.id) {
                        DiscussionUiState(discussion)
                    } else {
                        it
                    }
                } ?: emptyList()
        return HotDiscussionItems.ActivatedItem(newItems)
    }

    private fun modifyPopularDiscussion(discussion: Discussion): HotDiscussionItems.PopularItem {
        val newItems =
            items
                .filterIsInstance<HotDiscussionItems.PopularItem>()
                .firstOrNull()
                ?.items
                ?.map {
                    if (it.discussionId == discussion.id) {
                        DiscussionUiState(discussion)
                    } else {
                        it
                    }
                } ?: emptyList()
        return HotDiscussionItems.PopularItem(newItems)
    }
}
