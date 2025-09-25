package com.team.todoktodok.presentation.compose.discussion.model

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussionPage
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionsUiState
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionUiState
import com.team.todoktodok.presentation.compose.discussion.my.MyDiscussionUiState

data class DiscussionsUiState(
    val hotDiscussion: HotDiscussionUiState = HotDiscussionUiState(),
    val myDiscussion: MyDiscussionUiState = MyDiscussionUiState(),
    val allDiscussions: AllDiscussionsUiState = AllDiscussionsUiState(),
    val isUnreadNotification: Boolean = true,
) {
    fun refreshLatestDiscussion(): DiscussionsUiState =
        copy(allDiscussions = allDiscussions.refreshLatestDiscussion())

    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState =
        copy(
            allDiscussions =
                allDiscussions.addSearchDiscussion(
                    keyword,
                    newDiscussions,
                ),
        )

    fun addHotDiscussion(
        newItems: List<Discussion>,
        activatedDiscussion: ActivatedDiscussionPage,
    ): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.addHotDiscussions(newItems, activatedDiscussion))

    fun appendActivatedDiscussion(page: ActivatedDiscussionPage): DiscussionsUiState =
        copy(hotDiscussion = hotDiscussion.appendActivatedDiscussion(page))

    fun addMyDiscussion(
        createdDiscussion: List<Discussion>,
        participatedDiscussion: List<Discussion>,
    ): DiscussionsUiState =
        copy(myDiscussion = myDiscussion.addDiscussions(createdDiscussion, participatedDiscussion))

    fun addLatestDiscussion(page: LatestDiscussionPage): DiscussionsUiState =
        copy(allDiscussions = allDiscussions.addLatestDiscussion(page))

    fun modifyDiscussion(discussion: Discussion): DiscussionsUiState {
        val newHotDiscussion = hotDiscussion.modifyDiscussion(discussion)
        val newAllDiscussionsUiState = allDiscussions.modifyAllDiscussion(discussion)
        return copy(
            hotDiscussion = newHotDiscussion,
            allDiscussions = newAllDiscussionsUiState,
        )
    }

    fun clearSearchDiscussion() = copy(allDiscussions = allDiscussions.clearSearchDiscussion())

    fun modifySearchKeyword(keyword: String) =
        copy(allDiscussions = allDiscussions.modifyKeyword(keyword))

    fun removeDiscussion(discussionId: Long): DiscussionsUiState {
        val newHotDiscussion = hotDiscussion.removeDiscussion(discussionId)
        val newMyDiscussion = myDiscussion.removeDiscussion(discussionId)
        val newAllDiscussionsUiState = allDiscussions.removeAllDiscussion(discussionId)
        return copy(newHotDiscussion, newMyDiscussion, newAllDiscussionsUiState)
    }

    fun changeUnreadNotification(isExist: Boolean): DiscussionsUiState =
        copy(isUnreadNotification = isExist)

    val latestPageHasNext get() = allDiscussions.latestPageHasNext
    val latestPageNextCursor get() = allDiscussions.latestPageNextCursor
}
