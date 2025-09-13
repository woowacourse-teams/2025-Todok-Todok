package todoktodok.backend.discussion.application.service.command;

import todoktodok.backend.discussion.domain.DiscussionLike;

public record DiscussionLikeCreated(
        DiscussionLike discussionLike
) {
}
