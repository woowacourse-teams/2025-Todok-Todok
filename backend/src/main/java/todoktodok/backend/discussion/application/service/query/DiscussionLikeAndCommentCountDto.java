package todoktodok.backend.discussion.application.service.query;

import todoktodok.backend.discussion.domain.Discussion;

public record DiscussionLikeAndCommentCountDto(
        Discussion discussion,
        int totalCount
) {
}
