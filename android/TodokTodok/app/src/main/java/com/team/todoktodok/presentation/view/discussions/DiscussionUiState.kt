package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.latest.LatestDiscussion
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.data.core.ext.toLocalDateTime

data class DiscussionUiState(
    val item: Discussion,
    val opinionVisibility: Boolean = false,
) {
    val bookImage: String get() = item.bookImage
    val bookTitle: String get() = item.getBookTitle()
    val bookAuthor: String get() = item.getBookAuthor()
    val discussionTitle: String get() = item.discussionTitle
    val discussionOpinion: String get() = item.discussionOpinion
    val writerNickname: String get() = item.writerNickname
    val likeCount: String get() = item.likeCount.toString()
    val commentCount: String get() = item.commentCount.toString()
    val viewCount: String get() = item.viewCount.toString()
}

fun LatestDiscussion.toUiState(): DiscussionUiState {
    val discussion =
        Discussion(
            id = discussionId,
            book = book,
            discussionTitle = title,
            discussionOpinion = content,
            writer = User(author.id, Nickname(author.nickname)),
            createAt = createdAt,
            likeCount = likeCount,
            commentCount = commentCount,
            isLikedByMe = isLikedByMe,
        )
    return DiscussionUiState(discussion)
}

fun MemberDiscussion.toUiState(): DiscussionUiState {
    val writer = User(1, Nickname("페토"))
    val createAt = "2025-08-15T21:22:11".toLocalDateTime()
    val likeCount = 0
    val commentCount = 2
    val viewCount = 0
    val isLikedByMe = false

    return DiscussionUiState(
        item =
            Discussion(
                id = id,
                book = book,
                discussionTitle = discussionTitle,
                discussionOpinion = discussionOpinion,
                writer = writer,
                createAt = createAt,
                likeCount = likeCount,
                commentCount = commentCount,
                viewCount = viewCount,
                isLikedByMe = isLikedByMe,
            ),
    )
}
