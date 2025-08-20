package todoktodok.backend.discussion.application.dto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

public record DiscussionCursor(
        LocalDateTime lastCommentedAt,
        Long cursorId
) {

    private static final DiscussionCursor EMPTY = new DiscussionCursor(null, null);

    public static DiscussionCursor fromEncoded(final String cursor) {
        if (cursor == null) return empty();

        final String decoded = new String(Base64.getDecoder().decode(cursor));
        final String[] parts = decoded.split("_");
        final LocalDateTime lastCommentedAt = LocalDateTime.parse(parts[0]);
        final long cursorId = Long.parseLong(parts[1]);

        return new DiscussionCursor(lastCommentedAt, cursorId);
    }

    public static DiscussionCursor empty() {
        return EMPTY;
    }

    public String toEncoded(
        final LocalDateTime lastCommentedAt,
        final Long discussionId
    ) {
        final String cursorPayload = lastCommentedAt + "_" + discussionId;
        return Base64.getEncoder().encodeToString(cursorPayload.getBytes(StandardCharsets.UTF_8));
    }
}
