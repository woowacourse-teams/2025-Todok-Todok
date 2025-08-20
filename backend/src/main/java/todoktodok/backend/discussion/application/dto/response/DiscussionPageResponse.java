package todoktodok.backend.discussion.application.dto.response;

import java.util.List;
import todoktodok.backend.discussion.application.service.query.DiscussionCursorResponse;

public record DiscussionPageResponse(
        List<DiscussionCursorResponse> discussions,
        boolean hasNext,
        String nextCursor
) {
}
