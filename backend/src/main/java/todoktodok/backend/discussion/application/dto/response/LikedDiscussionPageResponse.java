package todoktodok.backend.discussion.application.dto.response;

import java.util.List;

public record LikedDiscussionPageResponse(
        List<DiscussionResponse> items,
        PageInfo pageInfo
) {
}
