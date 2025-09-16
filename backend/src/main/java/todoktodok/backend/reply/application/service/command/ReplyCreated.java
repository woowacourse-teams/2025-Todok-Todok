package todoktodok.backend.reply.application.service.command;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.reply.domain.Reply;

public record ReplyCreated(
        Long commentMemberId,
        Long authorId,
        String authorNickname,
        Long discussionId,
        String discussionTitle,
        Long commentId,
        Long replyId,
        String content
) {

    public ReplyCreated(
            final Discussion discussion,
            final Comment comment,
            final Reply reply,
            final Member replyMember
    ) {
        this(
                comment.getMember().getId(),
                replyMember.getId(),
                replyMember.getNickname(),
                discussion.getId(),
                discussion.getTitle(),
                comment.getId(),
                reply.getId(),
                reply.getContent()
        );
    }
}
