package todoktodok.backend.discussion.application.service.query;

public record DiscussionLikeCountDto(
        Long discussionId,
        int likeCount
) {
    public DiscussionLikeCountDto(
            final Long discussionId,
            final Long likeCount
    ) {
        this(
                discussionId,
                Math.toIntExact(likeCount)
        );
    }
}
