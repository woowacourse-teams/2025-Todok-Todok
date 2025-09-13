package todoktodok.backend.comment.application.service.command;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;

public record CommentCreated(
        Discussion discussion,
        Comment comment
) {
}
