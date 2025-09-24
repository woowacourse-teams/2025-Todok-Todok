package com.team.todoktodok.presentation.compose.discussion.latest

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.compose.component.DiscussionCardType
import com.team.todoktodok.presentation.xml.discussions.DiscussionUiState

data class LatestDiscussionsUiState(
    val discussions: List<DiscussionUiState> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val isRefreshing: Boolean = false,
    val latestPage: PageInfo = PageInfo.EMPTY,
) {
    fun refresh() = copy(discussions = emptyList(), latestPage = PageInfo.Companion.EMPTY, isRefreshing = true)

    fun append(page: LatestDiscussionPage): LatestDiscussionsUiState {
        val newDiscussion = discussions.toMutableList()

        val discussion =
            page
                .discussions
                .map { discussionItem ->
                    val keyword = discussions.firstOrNull()?.searchKeyword ?: ""
                    DiscussionUiState(discussionItem, keyword)
                }
        val pageInfo = page.pageInfo

        newDiscussion.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(discussions = newDiscussion, isRefreshing = false, latestPage = newLatestPage)
    }

    fun modify(newDiscussion: Discussion): LatestDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == newDiscussion.id) DiscussionUiState(newDiscussion) else it
                },
        )

    fun remove(discussionId: Long): LatestDiscussionsUiState {
        val newItems = discussions.filter { it.discussionId != discussionId }
        return copy(discussions = newItems)
    }

    val hasNext get() = latestPage.hasNext
    val nextCursor get() = latestPage.nextCursor
}
