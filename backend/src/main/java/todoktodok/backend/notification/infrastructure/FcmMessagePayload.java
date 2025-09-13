package todoktodok.backend.notification.infrastructure;

public record FcmMessagePayload(
        String title,
        String body,
        String discussionId,
        String commentId,
        String replyId,
        String memberNickname,
        String discussionTitle,
        String content,
        String type,
        String target
) {
    public FcmMessagePayload(
            final String title,
            final String body,
            final Long discussionId,
            final Long commentId,
            final Long replyId,
            final String memberNickname,
            final String discussionTitle,
            final String content,
            final String type,
            final String target
    ) {
        this(
                title,
                body,
                String.valueOf(discussionId),
                String.valueOf(commentId),
                String.valueOf(replyId),
                memberNickname,
                discussionTitle,
                content,
                type,
                target
        );
    }
}
