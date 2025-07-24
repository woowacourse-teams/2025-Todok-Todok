package com.example.todoktodok.fake

import com.example.domain.model.Comment
import com.example.domain.model.member.Nickname
import com.example.domain.model.member.User
import com.example.domain.repository.CommentRepository
import com.example.todoktodok.fixture.COMMENTS
import java.time.LocalDateTime

class FakeCommentRepository : CommentRepository {
    private val comments = COMMENTS.toMutableList()

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
            ),
        )
    }

    companion object {
        private val user = User(1, Nickname("동전"))
    }
}
