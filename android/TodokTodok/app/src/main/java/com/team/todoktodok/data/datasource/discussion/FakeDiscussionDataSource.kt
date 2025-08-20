package com.team.todoktodok.data.datasource.discussion

import com.team.domain.model.DiscussionFilter
import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.latest.AuthorResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionsResponse
import com.team.todoktodok.data.network.response.latest.PageInfoResponse
import retrofit2.Response

class FakeDiscussionDataSource : DiscussionRemoteDataSource {
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

    override suspend fun getDiscussion(id: Long): NetworkResult<DiscussionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscussions(
        type: DiscussionFilter,
        keyword: String?,
    ): NetworkResult<List<DiscussionResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveDiscussionRoom(
        bookId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun editDiscussionRoom(
        discussionId: Long,
        discussionTitle: String,
        discussionOpinion: String,
    ): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDiscussion(discussionId: Long): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLike(discussionId: Long): NetworkResult<LikeAction> {
        TODO("Not yet implemented")
    }

    override suspend fun reportDiscussion(discussionId: Long): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }
}

val LATEST_DISCUSSIONS_RESPONSE =
    List(100) { index ->
        LatestDiscussionResponse(
            author =
                AuthorResponse(
                    email = "user$index@example.com",
                    id = index.toLong(),
                    nickname = "User$index",
                    profileImage = "https://example.com/profiles/$index.png",
                ),
            book =
                BookResponse(
                    bookId = index.toLong(),
                    bookTitle = "Book Title $index - Subtitle $index",
                    bookAuthor = "Author $index (Some Info)",
                    bookImage = "https://example.com/books/$index.png",
                ),
            commentCount = (0..50).random(),
            content = "This is a sample discussion content for discussion #$index.",
            createdAt = "2025-08-20T10:59:48",
            discussionId = index.toLong(),
            isLikedByMe = listOf(true, false).random(),
            likeCount = (0..100).random(),
            title = "Discussion Title $index",
        )
    }
