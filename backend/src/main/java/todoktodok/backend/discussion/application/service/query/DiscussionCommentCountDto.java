package todoktodok.backend.discussion.application.service.query;

public record DiscussionCommentCountDto(
        Long discussionId,
        int commentCount,
        int replyCount
) {
    public DiscussionCommentCountDto(
            final Long discussionId,
            final Long commentCount,
            final Long replyCount
    ) {
        this(
                discussionId,
                commentCount.intValue(),
                replyCount.intValue()
        );
    }
}
