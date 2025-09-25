package todoktodok.backend.discussion.application.dto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public record ActiveDiscussionCursor(
        Long lastDiscussionLatestCommentId
) {
    private static final ActiveDiscussionCursor EMPTY = new ActiveDiscussionCursor(null);

    public static ActiveDiscussionCursor fromEncoded(final String cursor) {
        final String decoded = new String(Base64.getUrlDecoder().decode(cursor));
        final Long cursorId = Long.parseLong(decoded);

        return new ActiveDiscussionCursor(cursorId);
    }

    public static ActiveDiscussionCursor empty() {
        return EMPTY;
    }

    public String toEncoded() {
        if (lastDiscussionLatestCommentId == null) return null;
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(lastDiscussionLatestCommentId.toString().getBytes(StandardCharsets.UTF_8));
    }
}
