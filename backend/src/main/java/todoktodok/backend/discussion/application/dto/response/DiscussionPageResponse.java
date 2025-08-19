package todoktodok.backend.discussion.application.dto.response;

import java.util.List;

public record DiscussionPageResponse(
        List<DiscussionCursorResponse> discussions,
        boolean hasNext,
        String nextCursor
) {
}
