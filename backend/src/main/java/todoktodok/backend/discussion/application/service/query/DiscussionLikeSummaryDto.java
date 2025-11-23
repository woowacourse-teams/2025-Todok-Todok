package todoktodok.backend.discussion.application.service.query;

public record DiscussionLikeSummaryDto(
        Long discussionId,
        boolean isLikedByMe
) {
}
