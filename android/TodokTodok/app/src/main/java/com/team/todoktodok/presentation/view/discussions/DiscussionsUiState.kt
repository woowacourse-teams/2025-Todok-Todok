package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.DiscussionFilter
import com.team.domain.model.latest.LatestDiscussionPage
import com.team.domain.model.latest.PageInfo
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionItems
import com.team.todoktodok.presentation.view.discussions.my.adapter.MyDiscussionItems
import java.time.LocalDateTime

data class DiscussionsUiState(
    val hotDiscussionItems: List<HotDiscussionItems> =
        listOf(
            HotDiscussionItems.PopularItem(DISCUSSION_UI_STATE_WITH_OPINION),
            HotDiscussionItems.ActivatedItem(DISCUSSION_UI_STATE_WITHOUT_OPINION),
        ),
    val myDiscussions: List<MyDiscussionItems> = listOf(),
    val latestDiscussions: List<DiscussionUiState> = emptyList(),
    val latestPage: PageInfo = PageInfo(hasNext = false, nextCursor = ""),
    val searchKeyword: String = "",
    val filter: DiscussionFilter = DiscussionFilter.ALL,
    val isLoading: Boolean = false,
) {
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
        val discussion = page.discussions.map { it.toUiState() }
        val pageInfo = page.pageInfo

        val newLatestDiscussions = latestDiscussions.toMutableList()
        newLatestDiscussions.addAll(discussion)

        val newLatestPage = latestPage.copy(pageInfo.hasNext, pageInfo.nextCursor)

        return copy(latestDiscussions = newLatestDiscussions, latestPage = newLatestPage)
    }

    val latestPageHasNext get() = latestPage.hasNext
    val latestDiscussionsSize get() = latestDiscussions.size
    val myDiscussionsSize get() = myDiscussions.size

    fun toggleLoading() = copy(isLoading = !isLoading)

    companion object {
        private const val MY_DISCUSSION_SIZE = 3
    }
}

val DISCUSSIONS =
    mutableListOf(
        Discussion(
            id = 1L,
            discussionTitle = "JPA 성능 최적화",
            book = Book(1L, "자바 ORM 표준 JPA 프로그래밍 프로그래밍 프로그래밍 ", "김영한", ""),
            writer = User(1L, Nickname("홍길동")),
            createAt = LocalDateTime.of(2025, 7, 12, 12, 0),
            discussionOpinion = "fetch join을 남발하면 안됩니다.",
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 2L,
            discussionTitle = "코틀린 코루틴 완전 정복",
            book = Book(2L, "Kotlin in Action", "Dmitry Jemerov", ""),
            writer = User(2L, Nickname("박코루틴")),
            createAt = LocalDateTime.of(2025, 7, 13, 12, 0),
            discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 3L,
            discussionTitle = "MVVM 구조 제대로 이해하기",
            book = Book(3L, "안드로이드 아키텍처 가이드", "구글", ""),
            writer = User(3L, Nickname("김아키텍처")),
            createAt = LocalDateTime.of(2025, 7, 14, 12, 0),
            discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 4L,
            discussionTitle = "클린 코드란 무엇인가?",
            book = Book(4L, "Clean Code", "Robert C. Martin", ""),
            writer = User(4L, Nickname("이클린")),
            createAt = LocalDateTime.of(2025, 7, 15, 12, 0),
            discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
        Discussion(
            id = 5L,
            discussionTitle = "디자인 패턴 다시 보기",
            book = Book(5L, "Head First Design Patterns", "Eric Freeman", ""),
            writer = User(5L, Nickname("정디자인")),
            createAt = LocalDateTime.of(2025, 7, 16, 12, 0),
            discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
            likeCount = 0,
            commentCount = 0,
            isLikedByMe = false,
        ),
    )

val DISCUSSION_UI_STATE_WITH_OPINION =
    DISCUSSIONS.map {
        DiscussionUiState(
            item = it,
            opinionVisibility = true,
        )
    }

val DISCUSSION_UI_STATE_WITHOUT_OPINION =
    DISCUSSIONS.map {
        DiscussionUiState(
            item = it,
            opinionVisibility = false,
        )
    }
