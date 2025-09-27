package com.team.todoktodok.presentation.compose.discussion.all

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState

data class AllDiscussionsUiState(
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val mode: AllDiscussionMode = AllDiscussionMode.LATEST,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): AllDiscussionsUiState =
        copy(
            searchDiscussion = searchDiscussion.add(keyword, newDiscussions),
            mode = AllDiscussionMode.SEARCH,
        )

    fun modifyAllDiscussion(discussion: Discussion): AllDiscussionsUiState {
        val newSearchDiscussion = searchDiscussion.modify(discussion)
        return copy(searchDiscussion = newSearchDiscussion)
    }

    fun clearSearchDiscussion(): AllDiscussionsUiState = copy(searchDiscussion = searchDiscussion.clear(), mode = AllDiscussionMode.LATEST)

    fun modifyKeyword(keyword: String): AllDiscussionsUiState = copy(searchDiscussion = searchDiscussion.modifyKeyword(keyword))

    fun removeAllDiscussion(discussionId: Long): AllDiscussionsUiState {
        val newSearchDiscussion = searchDiscussion.remove(discussionId)
        return copy(searchDiscussion = newSearchDiscussion)
    }
}
