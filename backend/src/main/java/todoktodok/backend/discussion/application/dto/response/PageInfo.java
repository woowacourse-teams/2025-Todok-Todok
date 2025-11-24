package todoktodok.backend.discussion.application.dto.response;

public record PageInfo(
        boolean hasNext,
        String nextCursor,
        Long totalCount
) {
    public PageInfo(final boolean hasNext, final String nextCursor) {
        this(hasNext, nextCursor, null);
    }
}
