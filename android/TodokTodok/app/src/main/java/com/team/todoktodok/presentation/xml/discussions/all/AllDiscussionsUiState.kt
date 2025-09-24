package com.team.todoktodok.presentation.xml.discussions.all

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState

data class AllDiscussionsUiState(
    val latestDiscussion: LatestDiscussionsUiState = LatestDiscussionsUiState(),
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val mode: AllDiscussionMode = AllDiscussionMode.LATEST,
) {
    fun addLatestDiscussion(page: LatestDiscussionPage): AllDiscussionsUiState = copy(latestDiscussion = latestDiscussion.append(page))

    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): AllDiscussionsUiState =
        copy(
            searchDiscussion = searchDiscussion.add(keyword, newDiscussions),
            mode = AllDiscussionMode.SEARCH,
        )

    fun modifyAllDiscussion(discussion: Discussion): AllDiscussionsUiState {
        val newLatestDiscussion = latestDiscussion.modify(discussion)
        val newSearchDiscussion = searchDiscussion.modify(discussion)
        return copy(latestDiscussion = newLatestDiscussion, searchDiscussion = newSearchDiscussion)
    }

    fun clearSearchDiscussion(): AllDiscussionsUiState = copy(searchDiscussion = searchDiscussion.clear(), mode = AllDiscussionMode.LATEST)

    fun refreshLatestDiscussion(): AllDiscussionsUiState = copy(latestDiscussion = latestDiscussion.refresh())

    fun removeAllDiscussion(discussionId: Long): AllDiscussionsUiState {
        val newLatestDiscussion = latestDiscussion.remove(discussionId)
        val newSearchDiscussion = searchDiscussion.remove(discussionId)
        return copy(latestDiscussion = newLatestDiscussion, searchDiscussion = newSearchDiscussion)
    }

    val latestPageHasNext get() = latestDiscussion.hasNext
    val latestPageNextCursor get() = latestDiscussion.nextCursor
}
