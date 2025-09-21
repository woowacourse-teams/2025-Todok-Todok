package todoktodok.backend.reply.application.service.command;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.reply.domain.Reply;

public record ReplyLikeCreated(
        Long replyMemberId,
        Long authorId,
        String authorNickname,
        Long discussionId,
        String discussionTitle,
        Long commentId,
        Long replyId
) {

    public ReplyLikeCreated (
            final Member replyLikeMember,
            final Discussion discussion,
            final Comment comment,
            final Reply reply
    ) {
        this(
                reply.getMember().getId(),
                replyLikeMember.getId(),
                replyLikeMember.getNickname(),
                discussion.getId(),
                discussion.getTitle(),
                comment.getId(),
                reply.getId()
        );
    }
}
