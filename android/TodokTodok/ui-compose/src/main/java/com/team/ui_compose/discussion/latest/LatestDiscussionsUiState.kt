package com.team.ui_compose.discussion.latest

import com.team.domain.model.DiscussionPage
import com.team.domain.model.PageInfo
import com.team.ui_compose.component.DiscussionCardType
import com.team.ui_compose.discussion.model.DiscussionUiModel

data class LatestDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val isRefreshing: Boolean = false,
    val latestPage: PageInfo = PageInfo.EMPTY,
) {
    fun clearForRefresh(): LatestDiscussionsUiState = copy(discussions = emptyList(), latestPage = PageInfo.EMPTY, isRefreshing = true)

    fun appendDiscussion(page: DiscussionPage): LatestDiscussionsUiState {
        val newDiscussion =
            discussions.toMutableList().apply {
                addAll(page.discussions.map { discussion -> DiscussionUiModel(discussion) })
            }

        val pageInfo = page.pageInfo
        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(discussions = newDiscussion, isRefreshing = false, latestPage = newLatestPage)
    }

    val hasNext get() = latestPage.hasNext
    val nextCursor get() = latestPage.nextCursor
    val isLastPage get() = latestPage.isLastPage
}
