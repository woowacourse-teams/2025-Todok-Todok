package todoktodok.backend.discussion.application.dto.response;

import java.util.List;

public record ActiveDiscussionPageResponse(
        List<ActiveDiscussionResponse> items,
        PageInfo pageInfo
) {
}
