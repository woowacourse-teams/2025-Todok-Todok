package todoktodok.backend.comment.application.service.command;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public record CommentCreated(
        Long discussionMemberId,
        Long authorId,
        String authorNickname,
        Long discussionId,
        String discussionTitle,
        Long commentId,
        String content
) {

    public CommentCreated(
            final Discussion discussion,
            final Comment comment,
            final Member commentMember
            ) {
        this(
                discussion.getMember().getId(),
                commentMember.getId(),
                commentMember.getNickname(),
                discussion.getId(),
                discussion.getTitle(),
                comment.getId(),
                comment.getContent()
        );
    }
}
