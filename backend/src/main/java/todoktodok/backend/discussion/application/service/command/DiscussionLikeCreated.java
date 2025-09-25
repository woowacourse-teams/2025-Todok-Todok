package todoktodok.backend.discussion.application.service.command;

import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public record DiscussionLikeCreated(
        Long discussionMemberId,
        Long authorId,
        String authorNickname,
        Long discussionId,
        String discussionTitle
) {

    public DiscussionLikeCreated(
            final Discussion discussion,
            final Member discussionLikeMember
    ) {
        this(
                discussion.getMember().getId(),
                discussionLikeMember.getId(),
                discussionLikeMember.getNickname(),
                discussion.getId(),
                discussion.getTitle()
        );
    }
}
