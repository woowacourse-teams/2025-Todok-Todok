package com.team.todoktodok.fake

import com.team.domain.model.Comment
import com.team.domain.model.LikeStatus
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.fixture.COMMENTS
import java.time.LocalDateTime

class FakeCommentRepository : CommentRepository {
    private val comments = COMMENTS.toMutableList()

    override suspend fun getComment(
        discussionId: Long,
        commentId: Long,
    ): Comment {
        TODO("Not yet implemented")
    }

    override suspend fun getCommentsByDiscussionRoomId(id: Long): List<Comment> = comments

    override suspend fun saveComment(
        discussionId: Long,
        content: String,
    ) {
        comments.add(
            Comment(
                6,
                content,
                user,
                LocalDateTime.of(2008, 7, 23, 12, 10),
                likeCount = 0,
                replyCount = 0,
                isLikedByMe = false,
            ),
        )
    }

    override suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): LikeStatus {
        TODO("Not yet implemented")
    }

    override suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        content: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun report(
        discussionId: Long,
        commentId: Long,
    ) {
        TODO("Not yet implemented")
    }

    companion object {
        private val user = User(1, Nickname("동전"))
    }
}
