package todoktodok.backend.discussion.application.dto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public record ActiveDiscussionCursor(
        Long lastDiscussionLatestCommentId,
        Long lastDiscussionId
) {
    private static final String DELIMITER = "_";
    private static final ActiveDiscussionCursor EMPTY = new ActiveDiscussionCursor(null, null);

    public static ActiveDiscussionCursor fromEncoded(final String cursor) {
        final String decoded = new String(Base64.getUrlDecoder().decode(cursor));
        final String[] parts = decoded.split(DELIMITER);
        final Long lastDiscussionLatestCommentId = Long.parseLong(parts[0]);
        final Long discussionId = Long.parseLong(parts[1]);

        return new ActiveDiscussionCursor(lastDiscussionLatestCommentId, discussionId);
    }

    public static ActiveDiscussionCursor empty() {
        return EMPTY;
    }

    public String toEncoded() {
        final String cursorPayload = lastDiscussionLatestCommentId + DELIMITER + lastDiscussionId;
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(cursorPayload.getBytes(StandardCharsets.UTF_8));
    }
}
