package com.team.todoktodok.presentation.compose.main

import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.compose.discussion.model.AllDiscussionMode
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class MainUiState(
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val allDiscussionMode: AllDiscussionMode = AllDiscussionMode.LATEST,
    val bottomNavigationTab: MainDestination = MainDestination.Discussion,
    val hasUnreadNotification: Boolean = true,
    val searchBarVisible: Boolean = false,
    val isAllowed: Boolean = true,
    val isLoad: Boolean = false,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): MainUiState =
        copy(
            searchDiscussion = searchDiscussion.add(keyword, newDiscussions),
            allDiscussionMode = AllDiscussionMode.SEARCH,
        )

    fun modifyDiscussion(discussion: SerializationDiscussion): MainUiState =
        copy(
            searchDiscussion = searchDiscussion.modify(discussion),
        )

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

    fun changeAllowedNotification(isAllowed: Boolean): MainUiState = copy(isAllowed = isAllowed, isLoad = true)
}
