package todoktodok.backend.discussion.application.dto.response;

import java.util.List;

public record LatestDiscussionPageResponse(
        List<DiscussionResponse> items,
        PageInfo pageInfo
) {
}
