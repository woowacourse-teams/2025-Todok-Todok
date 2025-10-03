package com.team.todoktodok.presentation.compose.main

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionTabStatus
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class MainUiState(
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val allDiscussionMode: AllDiscussionMode = AllDiscussionMode.LATEST,
    val discussionTab: DiscussionTabStatus = DiscussionTabStatus.HOT,
    val bottomNavigationTab: MainDestination = MainDestination.Discussion,
    val hasUnreadNotification: Boolean = true,
    val searchBarVisible: Boolean = false,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): MainUiState =
        copy(
            searchDiscussion = searchDiscussion.add(keyword, newDiscussions),
            discussionTab = DiscussionTabStatus.ALL,
            allDiscussionMode = AllDiscussionMode.SEARCH,
        )

    fun modifyDiscussion(discussion: SerializationDiscussion): MainUiState = MainUiState()

    fun clearSearchDiscussion() =
        copy(
            searchDiscussion = searchDiscussion.clear(),
            allDiscussionMode = AllDiscussionMode.LATEST,
        )

    fun modifySearchKeyword(keyword: String) = copy(searchDiscussion = searchDiscussion.modifyKeyword(keyword))

    fun removeDiscussion(discussionId: Long): MainUiState {
        val newAllDiscussionsUiState = searchDiscussion.remove(discussionId)
        return copy(newAllDiscussionsUiState)
    }

    fun changeUnreadNotification(isExist: Boolean): MainUiState = copy(hasUnreadNotification = isExist)

    fun changeSearchBarVisibility(): MainUiState =
        if (searchBarVisible) {
            copy(
                searchBarVisible = false,
                allDiscussionMode = AllDiscussionMode.LATEST,
                searchDiscussion = searchDiscussion.clear(),
            )
        } else {
            copy(searchBarVisible = true)
        }

    fun changeBottomNavigationTab(destination: MainDestination): MainUiState = copy(bottomNavigationTab = destination)
}
