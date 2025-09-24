package todoktodok.backend.discussion.application.service.query;

public record DiscussionLikeSummaryDto(
        Long discussionId,
        int likeCount,
        boolean isLikedByMe
) {
    public DiscussionLikeSummaryDto(
            final Long discussionId,
            final Long likeCount,
            final boolean isLikedByMe
    ) {
        this(
                discussionId,
                Math.toIntExact(likeCount),
                isLikedByMe
        );
    }
}
