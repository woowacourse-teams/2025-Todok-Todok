package com.team.todoktodok.presentation.compose.discussion.latest

import com.team.domain.model.PageInfo
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.compose.core.component.DiscussionCardType
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class LatestDiscussionsUiState(
    val discussions: List<DiscussionUiModel> = emptyList(),
    val type: DiscussionCardType = DiscussionCardType.Default,
    val isRefreshing: Boolean = false,
    val latestPage: PageInfo = PageInfo.EMPTY,
) {
    fun clearForRefresh(): LatestDiscussionsUiState = copy(discussions = emptyList(), latestPage = PageInfo.EMPTY, isRefreshing = true)

    fun append(page: LatestDiscussionPage): LatestDiscussionsUiState {
        val newDiscussion =
            discussions.toMutableList().apply {
                addAll(page.discussions.map { discussion -> DiscussionUiModel(discussion) })
            }

        val pageInfo = page.pageInfo
        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(discussions = newDiscussion, isRefreshing = false, latestPage = newLatestPage)
    }

    fun modify(newDiscussion: SerializationDiscussion): LatestDiscussionsUiState =
        copy(
            discussions =
                discussions.map {
                    if (it.discussionId == newDiscussion.id) {
                        DiscussionUiModel(newDiscussion.toDomain())
                    } else {
                        it
                    }
                },
        )

    fun remove(discussionId: Long): LatestDiscussionsUiState {
        val newItems = discussions.filter { it.discussionId != discussionId }
        return copy(discussions = newItems)
    }

    val hasNext get() = latestPage.hasNext
    val nextCursor get() = latestPage.nextCursor
    val isLastPage get() = latestPage.isLastPage
}
