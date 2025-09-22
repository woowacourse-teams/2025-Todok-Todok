package com.team.todoktodok.presentation.xml.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.xml.discussions.hot.HotDiscussionUiState
import com.team.todoktodok.presentation.xml.discussions.latest.LatestDiscussionsUiState
import com.team.todoktodok.presentation.xml.discussions.my.MyDiscussionUiState
import com.team.todoktodok.presentation.xml.discussions.search.SearchDiscussionsUiState

data class DiscussionsUiState(
    val hotDiscussion: HotDiscussionUiState = HotDiscussionUiState(),
    val myDiscussion: MyDiscussionUiState = MyDiscussionUiState(),
    val latestDiscussion: LatestDiscussionsUiState = LatestDiscussionsUiState(),
    val searchDiscussion: SearchDiscussionsUiState = SearchDiscussionsUiState(),
    val isUnreadNotification: Boolean = false,
) {
    fun changeUnreadNotification(isExist: Boolean): DiscussionsUiState =
        copy(isUnreadNotification = isExist)

    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState = copy(searchDiscussion = searchDiscussion.add(keyword, newDiscussions))

    fun addHotDiscussion(
        newItems: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): DiscussionsUiState = copy(hotDiscussion = hotDiscussion.add(newItems, activatedDiscussion))

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.appendActivatedDiscussion(page))

    fun addMyDiscussion(
        createdDiscussion: List<Discussion>,
        participatedDiscussion: List<Discussion>,
    ): DiscussionsUiState =
        copy(myDiscussion = myDiscussion.add(createdDiscussion, participatedDiscussion))

    fun addLatestDiscussion(page: LatestDiscussionPage): DiscussionsUiState =
        copy(latestDiscussion = latestDiscussion.append(page))

    fun modifyDiscussion(discussion: Discussion): DiscussionsUiState {
        val newHotDiscussion = hotDiscussion.modifyDiscussion(discussion)
        val newLatestDiscussion = latestDiscussion.modifyDiscussion(discussion)
        val newSearchDiscussion = searchDiscussion.modifyDiscussion(discussion)
        return copy(
            hotDiscussion = newHotDiscussion,
            latestDiscussion = newLatestDiscussion,
            searchDiscussion = newSearchDiscussion,
        )
    }

    fun clearSearchDiscussion() = copy(searchDiscussion = searchDiscussion.clear())

    fun clearLatestDiscussion() = copy(latestDiscussion = latestDiscussion.clear())

    fun removeDiscussion(discussionId: Long): DiscussionsUiState {
        val newHotDiscussion = hotDiscussion.removeDiscussion(discussionId)
        val newMyDiscussion = myDiscussion.removeDiscussion(discussionId)
        val newLatestDiscussion = latestDiscussion.removeDiscussion(discussionId)
        val newSearchDiscussion = searchDiscussion.removeDiscussion(discussionId)
        return copy(newHotDiscussion, newMyDiscussion, newLatestDiscussion, newSearchDiscussion)
    }

    val latestPageHasNext get() = latestDiscussion.hasNext
    val latestPageNextCursor get() = latestDiscussion.nextCursor
}
