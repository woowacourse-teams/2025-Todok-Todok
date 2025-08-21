package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.domain.model.member.MemberDiscussion
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionItems
import com.team.todoktodok.presentation.view.discussions.my.adapter.MyDiscussionItems

data class DiscussionsUiState(
    val hotDiscussionItems: List<HotDiscussionItems> = emptyList(),
    val myDiscussions: List<MyDiscussionItems> = listOf(),
    val latestDiscussions: List<DiscussionUiState> = emptyList(),
    val latestPage: PageInfo = PageInfo.EMPTY,
    val searchKeyword: String = EMPTY_SEARCH_KEYWORD,
    val loadBySearch: Boolean = false,
    val isLoading: Boolean = false,
) {
    fun addSearchDiscussion(
        keyword: String,
        newDiscussions: List<Discussion>,
    ): DiscussionsUiState {
        val discussion = newDiscussions.map { DiscussionUiState(it) }
        return copy(
            searchKeyword = keyword,
            latestDiscussions = discussion,
            latestPage = PageInfo.EMPTY,
            loadBySearch = true,
        )
    }

    fun addHotDiscussion(
        hotDiscussions: List<Discussion>,
        activatedDiscussion: List<Discussion>,
    ): DiscussionsUiState {
        val hotDiscussion =
            buildList {
                val popularItems = hotDiscussions.map { DiscussionUiState(it, true) }
                val activatedItems = activatedDiscussion.map { DiscussionUiState(it, false) }

                add(HotDiscussionItems.PopularItem(popularItems))
                add(HotDiscussionItems.ActivatedHeaderItem)
                add(HotDiscussionItems.ActivatedItem(activatedItems))
            }

        return copy(hotDiscussionItems = hotDiscussion)
    }

    fun addMyDiscussion(
        createdDiscussion: List<MemberDiscussion>,
        participatedDiscussion: List<MemberDiscussion>,
    ): DiscussionsUiState {
        val created = createdDiscussion.take(MY_DISCUSSION_SIZE).map { it.toUiState() }
        val participated = participatedDiscussion.take(MY_DISCUSSION_SIZE).map { it.toUiState() }

        val updatedList =
            buildList {
                if (created.isNotEmpty()) add(MyDiscussionItems.CreatedDiscussionItem(created))
                if (participated.isNotEmpty()) {
                    if (created.isNotEmpty()) add(MyDiscussionItems.DividerItem)
                    add(MyDiscussionItems.ParticipatedDiscussionItem(participated))
                }
            }

        return copy(myDiscussions = updatedList)
    }

    fun addLatestDiscussion(page: LatestDiscussionPage): DiscussionsUiState {
        val newDiscussion = latestDiscussions.toMutableList()
        if (loadBySearch) newDiscussion.clear()

        val discussion = page.discussions.map { it.toUiState() }
        val pageInfo = page.pageInfo

        newDiscussion.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(
            latestDiscussions = newDiscussion,
            latestPage = newLatestPage,
            loadBySearch = false,
        )
    }

    val latestPageHasNext get() = latestPage.hasNext

    fun toggleLoading() = copy(isLoading = !isLoading)

    companion object {
        private const val MY_DISCUSSION_SIZE = 3
        private const val EMPTY_SEARCH_KEYWORD = ""
    }
}
