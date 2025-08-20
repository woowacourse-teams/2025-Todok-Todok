package todoktodok.backend.discussion.application.dto.response;

import java.util.List;

public record SlicedDiscussionResponse(
        List<DiscussionResponse> items,
        PageInfo pageInfo
) {
}
