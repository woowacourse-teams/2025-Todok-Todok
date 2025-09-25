package todoktodok.backend.book.application.dto.response;

import java.util.List;

public record LatestAladinBookPageResponse(
        List<AladinBookResponse> items,
        PageInfo pageInfo,
        int totalSize
) {
}
