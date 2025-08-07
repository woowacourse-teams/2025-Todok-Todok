package todoktodok.backend.discussion.application.service.query;

public record DiscussionCommentCountDto(
        Long discussionId,
        int commentCount
) {
    public DiscussionCommentCountDto(
            final Long discussionId,
            final Long commentCount
    ) {
        this(
                discussionId,
                commentCount.intValue()
        );
    }
}
