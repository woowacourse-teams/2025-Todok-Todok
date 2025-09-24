package com.team.todoktodok.presentation.compose.discussion.latest

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

data class LatestDiscussionsUiState(
    val items: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val isRefreshing: Boolean = false,
    val latestPage: PageInfo = PageInfo.EMPTY,
) {
    fun refresh() = copy(items = emptyList(), latestPage = PageInfo.Companion.EMPTY, isRefreshing = true)

    fun append(page: LatestDiscussionPage): LatestDiscussionsUiState {
        val newDiscussion = items.toMutableList()

        val discussion =
            page
                .discussions
                .map { discussionItem ->
                    val keyword = items.firstOrNull()?.searchKeyword ?: ""
                    DiscussionUiState(discussionItem, keyword)
                }
        val pageInfo = page.pageInfo

        newDiscussion.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(items = newDiscussion, isRefreshing = false, latestPage = newLatestPage)
    }

    fun modifyDiscussion(newDiscussion: Discussion): LatestDiscussionsUiState =
        copy(
            items =
                items.map {
                    if (it.discussionId == newDiscussion.id) DiscussionUiState(newDiscussion) else it
                },
        )

    fun removeDiscussion(discussionId: Long): LatestDiscussionsUiState {
        val newItems = items.filter { it.discussionId != discussionId }
        return copy(items = newItems)
    }

    val hasNext get() = latestPage.hasNext
    val nextCursor get() = latestPage.nextCursor
}
