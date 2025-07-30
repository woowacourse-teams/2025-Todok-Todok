package todoktodok.backend.book.infrastructure.aladin;

public record AladinItemResponse(
        String title,
        String description,
        String author,
        String publisher,
        String isbn13,
        String cover
) {
}
