package com.team.todoktodok.presentation.view.discussions.latest

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState
import com.team.todoktodok.presentation.view.discussions.toUiState

data class LatestDiscussionsUiState(
    val items: List<DiscussionUiState> = emptyList(),
    val latestPage: PageInfo = PageInfo.EMPTY,
) {
    fun append(page: LatestDiscussionPage): LatestDiscussionsUiState {
        val newDiscussion = items.toMutableList()

        val discussion = page.discussions.map { it.toUiState() }
        val pageInfo = page.pageInfo

        newDiscussion.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(newDiscussion, newLatestPage)
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

    fun clear() = copy(items = emptyList(), latestPage = PageInfo.EMPTY)

    val hasNext get() = latestPage.hasNext
    val nextCursor get() = latestPage.nextCursor
}
