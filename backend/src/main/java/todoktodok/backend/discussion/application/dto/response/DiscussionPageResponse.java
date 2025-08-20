package todoktodok.backend.discussion.application.dto.response;

import java.util.List;
import todoktodok.backend.discussion.application.service.query.DiscussionCursorDto;

public record DiscussionPageResponse(
        List<DiscussionCursorDto> items,
        PageInfo pageInfo
) {
}
