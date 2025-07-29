package todoktodok.backend.book.application.dto.response;

import todoktodok.backend.book.infrastructure.aladin.AladinItemResponse;

public record AladinBookResponse(
        String bookId,
        String bookTitle,
        String bookAuthor,
        String bookImage
) {

    public AladinBookResponse(AladinItemResponse aladinItemResponse) {
        this(
                aladinItemResponse.isbn13(),
                aladinItemResponse.title(),
                aladinItemResponse.author(),
                aladinItemResponse.cover()
        );
    }
}
