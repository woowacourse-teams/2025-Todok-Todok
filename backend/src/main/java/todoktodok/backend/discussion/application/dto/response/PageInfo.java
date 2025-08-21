package todoktodok.backend.discussion.application.dto.response;

public record PageInfo(
        boolean hasNext,
        String nextCursor
) {
}
