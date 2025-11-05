package com.team.data.fixture

import com.team.data.network.response.discussion.BookResponse
import com.team.data.network.response.latest.LatestDiscussionResponse
import com.team.data.network.response.latest.MemberResponse

val LATEST_DISCUSSIONS_RESPONSE_FIXTURE =
    List(100) { index ->
        LatestDiscussionResponse(
            member =
                MemberResponse(
                    memberId = index.toLong(),
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
            commentCount = (0..50).random().toLong(),
            discussionOpinion = "This is a sample discussion content for discussion #$index.",
            createdAt = "2025-08-20T10:59:48",
            discussionId = index.toLong(),
            isLikedByMe = listOf(true, false).random(),
            likeCount = (0..100).random().toLong(),
            viewCount = 0,
            discussionTitle = "Discussion Title $index",
        )
    }
