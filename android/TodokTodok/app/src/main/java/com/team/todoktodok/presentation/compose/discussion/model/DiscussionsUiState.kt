package com.team.todoktodok.presentation.compose.discussion.model

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class DiscussionsUiState(
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val allDiscussionMode: AllDiscussionMode = AllDiscussionMode.LATEST,
    val isUnreadNotification: Boolean = true,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState =
        copy(
            searchDiscussion = searchDiscussion.add(keyword, newDiscussions),
            allDiscussionMode = AllDiscussionMode.SEARCH,
        )

    fun modifyDiscussion(discussion: SerializationDiscussion): DiscussionsUiState {
        val newDiscussion = discussion.toDomain()
        val newSearchDiscussionsUiState = searchDiscussion.modify(newDiscussion)

        return copy(
            searchDiscussion = newSearchDiscussionsUiState,
        )
    }

    fun clearSearchDiscussion() =
        copy(
            searchDiscussion = searchDiscussion.clear(),
            allDiscussionMode = AllDiscussionMode.LATEST,
        )

    fun modifySearchKeyword(keyword: String) = copy(searchDiscussion = searchDiscussion.modifyKeyword(keyword))

    fun removeDiscussion(discussionId: Long): DiscussionsUiState {
        val newAllDiscussionsUiState = searchDiscussion.remove(discussionId)
        return copy(newAllDiscussionsUiState)
    }

    fun changeUnreadNotification(isExist: Boolean): DiscussionsUiState = copy(isUnreadNotification = isExist)
}
