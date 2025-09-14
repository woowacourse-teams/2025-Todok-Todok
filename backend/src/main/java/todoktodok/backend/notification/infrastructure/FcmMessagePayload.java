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
                safeStringValue(discussionId),
                safeStringValue(commentId),
                safeStringValue(replyId),
                memberNickname,
                discussionTitle,
                content,
                type,
                target
        );
    }

    private static String safeStringValue(final Long id) {
        if (id == null) {
            return "null";
        }
        return String.valueOf(id);
    }
}
