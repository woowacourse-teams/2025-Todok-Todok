package com.team.todoktodok.presentation.compose.discussion.latest

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo

data class LatestDiscussionsUiState(
    val items: List<Discussion> = emptyList(),
    val latestPage: PageInfo = PageInfo.EMPTY,
) {
    fun append(page: LatestDiscussionPage): LatestDiscussionsUiState {
        val newDiscussion = items.toMutableList()

        val discussion = page.discussions
        val pageInfo = page.pageInfo

        newDiscussion.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(newDiscussion, newLatestPage)
    }

    fun modifyDiscussion(newDiscussion: Discussion): LatestDiscussionsUiState =
        copy(items = items.map { if (it.id == newDiscussion.id) newDiscussion else it })

    fun removeDiscussion(discussionId: Long): LatestDiscussionsUiState {
        val newItems = items.filter { it.id != discussionId }
        return copy(items = newItems)
    }

    fun clear() = copy(items = emptyList(), latestPage = PageInfo.EMPTY)

    val hasNext get() = latestPage.hasNext
    val nextCursor get() = latestPage.nextCursor
}
