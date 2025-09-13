package todoktodok.backend.comment.application.service.command;

import todoktodok.backend.comment.domain.CommentLike;

public record CommentLikeCreated(
        CommentLike commentLike
) {
}
