package todoktodok.backend.book.presentation.fixture;

import todoktodok.backend.book.domain.Book;

public class BookFixture {

    public static Book create(
            final String title,
            final String author,
            final String publisher,
            final String isbn
    ) {
        return Book.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .isbn(isbn)
                .build();
    }
}
