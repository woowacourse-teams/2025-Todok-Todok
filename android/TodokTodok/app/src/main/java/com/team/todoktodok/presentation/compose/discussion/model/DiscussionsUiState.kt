package com.team.todoktodok.presentation.compose.discussion.model

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionUiState
import com.team.todoktodok.presentation.compose.discussion.my.MyDiscussionUiState
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionsUiState
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

data class DiscussionsUiState(
    val hotDiscussion: HotDiscussionUiState = HotDiscussionUiState(),
    val myDiscussion: MyDiscussionUiState = MyDiscussionUiState(),
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

    fun addPopularDiscussion(discussions: List<Discussion>): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.addPopularDiscussions(discussions))

    fun addActivatedDiscussion(page: ActivatedDiscussionPage): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.addActivatedDiscussions(page))

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.appendActivatedDiscussion(page))

    fun addCreatedDiscussion(discussions: List<Discussion>): DiscussionsUiState =
        copy(myDiscussion = myDiscussion.addCreatedDiscussions(discussions))

    fun addParticipatedDiscussion(discussions: List<Discussion>): DiscussionsUiState =
        copy(myDiscussion = myDiscussion.addParticipatedDiscussions(discussions))

    fun modifyDiscussion(discussion: SerializationDiscussion): DiscussionsUiState {
        val newDiscussion = discussion.toDomain()
        val newHotDiscussion = hotDiscussion.modifyDiscussion(newDiscussion)
        val newSearchDiscussionsUiState = searchDiscussion.modify(newDiscussion)

        return copy(
            hotDiscussion = newHotDiscussion,
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
        val newHotDiscussion = hotDiscussion.removeDiscussion(discussionId)
        val newMyDiscussion = myDiscussion.removeDiscussion(discussionId)
        val newAllDiscussionsUiState = searchDiscussion.remove(discussionId)
        return copy(newHotDiscussion, newMyDiscussion, newAllDiscussionsUiState)
    }

    fun changeUnreadNotification(isExist: Boolean): DiscussionsUiState = copy(isUnreadNotification = isExist)
}
