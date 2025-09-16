package todoktodok.backend.book.application.dto.response;

public record PageInfo(
        boolean hasNext,
        String nextCursor
) {
}
