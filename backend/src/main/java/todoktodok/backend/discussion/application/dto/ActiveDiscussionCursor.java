package todoktodok.backend.discussion.application.dto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public record ActiveDiscussionCursor(
        Long lastDiscussionLatestCommentId
) {
    private static final ActiveDiscussionCursor EMPTY = new ActiveDiscussionCursor(null);

    public static ActiveDiscussionCursor fromEncoded(final String cursor) {

        try {
            final String decoded = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
            final Long cursorId = Long.parseLong(decoded);
            return new ActiveDiscussionCursor(cursorId);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Base64로 디코드할 수 없는 cursor 값입니다: cursor = %s", cursor));
        }
    }

    public static ActiveDiscussionCursor empty() {
        return EMPTY;
    }

    public String toEncoded() {
        if (lastDiscussionLatestCommentId == null) {
            return null;
        }
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(lastDiscussionLatestCommentId.toString().getBytes(StandardCharsets.UTF_8));
    }
}
