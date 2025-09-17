package todoktodok.backend.comment.application.service.command;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public record CommentLikeCreated(
        Long commentMemberId,
        Long authorId,
        String authorNickname,
        Long discussionId,
        String discussionTitle,
        Long commentId
) {

    public CommentLikeCreated(
            final Discussion discussion,
            final Comment comment,
            final Member commentLikeMember
    ) {
        this(
                comment.getMember().getId(),
                commentLikeMember.getId(),
                commentLikeMember.getNickname(),
                discussion.getId(),
                discussion.getTitle(),
                comment.getId()
        );
    }
}
