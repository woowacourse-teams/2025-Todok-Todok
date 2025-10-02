package com.team.todoktodok.presentation.compose.discussion.model

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.compose.main.MainDestination
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class DiscussionsUiState(
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val allDiscussionMode: AllDiscussionMode = AllDiscussionMode.LATEST,
    val discussionTab: DiscussionTabStatus = DiscussionTabStatus.HOT,
    val bottomNavigationTab: MainDestination = MainDestination.Discussion,
    val isUnreadNotification: Boolean = true,
    val searchBarVisible: Boolean = false,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState =
        copy(
            searchDiscussion = searchDiscussion.add(keyword, newDiscussions),
            discussionTab = DiscussionTabStatus.ALL,
            allDiscussionMode = AllDiscussionMode.SEARCH,
        )

    fun modifyDiscussion(discussion: SerializationDiscussion): DiscussionsUiState = DiscussionsUiState()

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

    fun changeSearchBarVisibility(): DiscussionsUiState =
        copy(
            searchBarVisible = !searchBarVisible,
            searchDiscussion = if (searchBarVisible) searchDiscussion else searchDiscussion.clear(),
        )

    fun changeBottomNavigationTab(destination: MainDestination): DiscussionsUiState = copy(bottomNavigationTab = destination)
}
