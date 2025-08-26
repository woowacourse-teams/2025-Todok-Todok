package com.team.todoktodok.presentation.view.discussions

import com.team.domain.model.Discussion
import com.team.domain.model.active.ActivatedDiscussion
import com.team.domain.model.latest.LatestDiscussion
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User

data class DiscussionUiState(
    private val item: Discussion,
    val opinionVisibility: Boolean = false,
    val writerVisibility: Boolean = true,
) {
    val bookImage: String get() = item.bookImage
    val bookTitle: String get() = item.getBookTitle()
    val bookAuthor: String get() = item.getBookAuthor()
    val discussionId: Long get() = item.id
    val discussionTitle: String get() = item.discussionTitle
    val discussionOpinion: String get() = item.discussionOpinion
    val writerNickname: String get() = item.writerNickname
    val writerProfileImage: String get() = item.writer.profileImage

    val likeCount: String get() = item.likeCount.toString()
    val commentCount: String get() = item.commentCount.toString()
    val viewCount: String get() = item.viewCount.toString()

    val isLikedByMe: Boolean get() = item.isLikedByMe
}

fun LatestDiscussion.toUiState(): DiscussionUiState {
    val discussion =
        Discussion(
            id = discussionId,
            book = book,
            discussionTitle = title,
            discussionOpinion = content,
            writer = User(author.memberId, Nickname(author.nickname), author.profileImage),
            createAt = createdAt,
            likeCount = likeCount,
            commentCount = commentCount,
            isLikedByMe = isLikedByMe,
        )
    return DiscussionUiState(discussion)
}

fun ActivatedDiscussion.toUiState(): DiscussionUiState {
    val discussion =
        Discussion(
            id = discussionId,
            book = book,
            discussionTitle = discussionTitle,
            discussionOpinion = discussionOpinion,
            writer = User(writer.id, writer.nickname, writer.profileImage),
            createAt = createdAt,
            likeCount = likeCount,
            commentCount = commentCount,
            isLikedByMe = isLikedByMe,
        )
    return DiscussionUiState(discussion)
}
