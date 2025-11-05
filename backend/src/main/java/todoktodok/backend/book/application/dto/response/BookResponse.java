package todoktodok.backend.book.application.dto.response;

import todoktodok.backend.book.domain.Book;

public record BookResponse(
        Long bookId,
        String bookTitle,
        String bookAuthor,
        String bookImage,
        String bookPublisher,
        String bookSummary
) {

    public BookResponse(final Book book) {
        this(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getImage(),
                book.getPublisher(),
                book.getSummary()
        );
    }
}
