package todoktodok.backend.reply.application.service.command;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.reply.domain.Reply;

public record ReplyCreated(
        Discussion discussion,
        Comment comment,
        Reply reply
) {
}
