package todoktodok.backend.discussion.application.service.event;

public record DiscussionViewEvent(
        Long memberId,
        Long discussionId
) {
}
