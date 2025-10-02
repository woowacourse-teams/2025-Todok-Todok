package com.team.todoktodok.presentation.compose.discussion.all

import com.team.domain.model.Discussion
import com.team.domain.model.PageInfo
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class AllDiscussionUiState(
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val latestDiscussion: LatestDiscussionsUiState = LatestDiscussionsUiState(),
    val mode: AllDiscussionMode = AllDiscussionMode.LATEST,
) {
    fun refresh() =
        copy(
            latestDiscussion =
                latestDiscussion.copy(
                    discussions = emptyList(),
                    latestPage = PageInfo.Companion.EMPTY,
                    isRefreshing = true,
                ),
        )

    fun appendLatestDiscussion(page: LatestDiscussionPage): AllDiscussionUiState {
        val newDiscussion =
            latestDiscussion.discussions.toMutableList().apply {
                addAll(page.discussions.map { discussion -> DiscussionUiState(discussion) })
            }

        return copy(
            latestDiscussion =
                latestDiscussion.copy(
                    discussions = newDiscussion,
                    isRefreshing = false,
                    latestPage = page.pageInfo,
                ),
        )
    }

    fun modifyDiscussion(newDiscussion: SerializationDiscussion): AllDiscussionUiState =
        copy(
            searchDiscussion = searchDiscussion.modify(newDiscussion),
            latestDiscussion = latestDiscussion.modify(newDiscussion),
        )

    fun removeDiscussion(discussionId: Long): AllDiscussionUiState =
        copy(
            searchDiscussion = searchDiscussion.remove(discussionId),
            latestDiscussion = latestDiscussion.remove(discussionId),
        )

    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): AllDiscussionUiState = copy(searchDiscussion = searchDiscussion.add(keyword, newDiscussions))

    fun clearSearchDiscussion(): AllDiscussionUiState = copy(searchDiscussion = searchDiscussion.clear())

    fun modifyKeyword(keyword: String) = copy(searchDiscussion = searchDiscussion.modifyKeyword(keyword))

    val nextCursor get() = latestDiscussion.latestPage.nextCursor
    val isLastPage get() = latestDiscussion.latestPage.isLastPage
}
