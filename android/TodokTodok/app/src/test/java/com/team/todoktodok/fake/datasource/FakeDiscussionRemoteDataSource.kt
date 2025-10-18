package com.team.todoktodok.fake.datasource

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.data.datasource.discussion.DiscussionRemoteDataSource
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.discussion.MemberResponse
import com.team.todoktodok.data.network.response.discussion.liked.LikedDiscussionPageResponse
import com.team.todoktodok.data.network.response.discussion.page.ActivatedDiscussion
import com.team.todoktodok.data.network.response.discussion.page.ActivatedDiscussionPageResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionsResponse
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import com.team.todoktodok.fixture.LATEST_DISCUSSIONS_RESPONSE
import retrofit2.Response

class FakeDiscussionRemoteDataSource : DiscussionRemoteDataSource {
    private val discussionResponses: MutableList<DiscussionResponse> =
        mutableListOf(
            DiscussionResponse(
                discussionId = 1,
                discussionTitle = "JPA 성능 최적화",
                book = BookResponse("김영한", 1, "", "자바 ORM 표준 JPA 프로그래밍"),
                discussionOpinion = "fetch join을 남발하면 안됩니다.",
                createdAt = "2025-07-12T12:00:00",
                member = MemberResponse(1, "홍길동", ""),
                likeCount = 0,
                viewCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 2,
                discussionTitle = "코틀린 코루틴 완전 정복",
                book = BookResponse("Dmitry Jemerov", 2, "", "Kotlin in Action"),
                discussionOpinion = "suspend fun과 launch 차이를 이해해야 합니다.",
                createdAt = "2025-07-13T12:00:00",
                member = MemberResponse(2, "박코루틴", ""),
                likeCount = 0,
                viewCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 3,
                discussionTitle = "MVVM 구조 제대로 이해하기",
                book = BookResponse("구글", 3, "", "안드로이드 아키텍처 가이드"),
                discussionOpinion = "UI와 로직을 분리해 유지보수가 쉬워집니다.",
                createdAt = "2025-07-14T12:00:00",
                member = MemberResponse(3, "김아키텍처", ""),
                likeCount = 0,
                viewCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 4,
                discussionTitle = "클린 코드란 무엇인가?",
                book = BookResponse("Robert C. Martin", 4, "", "Clean Code"),
                discussionOpinion = "의도를 드러내는 코드가 중요합니다.",
                createdAt = "2025-07-15T12:00:00",
                member = MemberResponse(4, "이클린", ""),
                likeCount = 0,
                viewCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
            DiscussionResponse(
                discussionId = 5,
                discussionTitle = "디자인 패턴 다시 보기",
                book = BookResponse("Eric Freeman", 5, "", "Head First Design Patterns"),
                discussionOpinion = "상황에 맞는 패턴 선택이 중요합니다.",
                createdAt = "2025-07-16T12:00:00",
                member = MemberResponse(5, "정디자인", ""),
                likeCount = 0,
                viewCount = 0,
                commentCount = 0,
                isLikedByMe = false,
            ),
        )

    override suspend fun getSearchDiscussion(keyword: String): NetworkResult<List<DiscussionResponse>> {
        val filtered =
            discussionResponses.filter {
                it.discussionTitle.contains(keyword, ignoreCase = true) ||
                    it.discussionOpinion.contains(keyword, ignoreCase = true)
            }
        return NetworkResult.Success(filtered)
    }

    override suspend fun getActivatedDiscussion(
        period: Int,
        size: Int,
        cursor: String?,
    ): NetworkResult<ActivatedDiscussionPageResponse> {
        val startIndex = cursor?.toIntOrNull() ?: 0
        val endIndex = (startIndex + size).coerceAtMost(discussionResponses.size)
        val pageDiscussions =
            discussionResponses.subList(startIndex, endIndex).map {
                ActivatedDiscussion(
                    discussionId = it.discussionId,
                    book = it.book,
                    member = it.member,
                    createdAt = it.createdAt,
                    discussionTitle = it.discussionTitle,
                    discussionOpinion = it.discussionOpinion,
                    viewCount = it.viewCount,
                    likeCount = it.likeCount,
                    commentCount = it.commentCount,
                    isLikedByMe = it.isLikedByMe,
                )
            }

        val hasNext = endIndex < discussionResponses.size
        val nextCursor = if (hasNext) endIndex.toString() else ""

        val pageInfo = PageInfoResponse(hasNext, nextCursor)
        val page =
            ActivatedDiscussionPageResponse(
                items = pageDiscussions,
                pageInfo = pageInfo,
            )

        return NetworkResult.Success(page)
    }

    override suspend fun getLikedDiscussion(
        size: Int,
        cursor: String?,
    ): NetworkResult<LikedDiscussionPageResponse> {
        val likedDiscussions = discussionResponses.filter { it.isLikedByMe }

        val startIndex = cursor?.toIntOrNull() ?: 0

        val endIndex = (startIndex + size).coerceAtMost(likedDiscussions.size)
        val pageDiscussions = likedDiscussions.subList(startIndex, endIndex)

        val hasNext = endIndex < likedDiscussions.size
        val nextCursor = if (hasNext) endIndex.toString() else ""
        val pageResponse =
            LikedDiscussionPageResponse(
                items = pageDiscussions,
                pageInfo = PageInfoResponse(hasNext, nextCursor),
            )

        return NetworkResult.Success(pageResponse)
    }

    override suspend fun getHotDiscussion(
        period: Int,
        count: Int,
    ): NetworkResult<List<DiscussionResponse>> = NetworkResult.Success(discussionResponses)

    override suspend fun getLatestDiscussions(
        size: Int,
        cursor: String?,
    ): NetworkResult<LatestDiscussionsResponse> {
        val startIndex = cursor?.toIntOrNull() ?: 0
        val endIndex = (startIndex + size).coerceAtMost(LATEST_DISCUSSIONS_RESPONSE.size)
        val pageDiscussions = LATEST_DISCUSSIONS_RESPONSE.subList(startIndex, endIndex)

        val hasNext = endIndex < LATEST_DISCUSSIONS_RESPONSE.size
        val nextCursor = if (hasNext) endIndex.toString() else ""

        val pageInfo = PageInfoResponse(hasNext, nextCursor)
        val page = LatestDiscussionsResponse(pageDiscussions, pageInfo)

        return NetworkResult.Success(page)
    }

    override suspend fun fetchDiscussion(id: Long): NetworkResult<DiscussionResponse> =
        NetworkResult.Success(
            discussionResponses.find { id == it.discussionId }
                ?: throw IllegalArgumentException(),
        )

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> = Response.success(Unit)

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): NetworkResult<Unit> = NetworkResult.Success(Unit)

    override suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit> = NetworkResult.Success(Unit)

    override suspend fun toggleLike(discussionId: Long): NetworkResult<LikeAction> {
        val idx = discussionResponses.indexOfFirst { it.discussionId == discussionId }
        if (idx < 0) {
            return NetworkResult.Failure(TodokTodokExceptions.EmptyBodyException)
        }
        val cur = discussionResponses[idx]
        val likeAction = if (!cur.isLikedByMe) LikeAction.LIKE else LikeAction.UNLIKE
        val nowLiked = !cur.isLikedByMe
        val newCount = (cur.likeCount + if (nowLiked) 1 else -1).coerceAtLeast(0)

        discussionResponses[idx] =
            cur.copy(
                isLikedByMe = nowLiked,
                likeCount = newCount,
            )
        return NetworkResult.Success(likeAction)
    }

    override suspend fun reportDiscussion(
        discussionId: Long,
        reason: String,
    ): NetworkResult<Unit> {
        val discussionResponse = discussionResponses.find { it.discussionId == discussionId }
        return if (discussionResponse != null) {
            NetworkResult.Success(Unit)
        } else {
            NetworkResult.Failure(TodokTodokExceptions.EmptyBodyException)
        }
    }
}
