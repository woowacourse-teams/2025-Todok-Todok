package todoktodok.backend.book.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.book.domain.Book;

public class BookTest {

    @Test
    @DisplayName("도서 제목이 비어있으면 예외가 발생한다")
    void validateEmpty_tooShortTitle_fail() {
        // given
        final String title = "";

        // when - then
        assertThatThrownBy(
                () -> Book.builder()
                        .title(title)
                        .summary("summary")
                        .author("author")
                        .publisher("publisher")
                        .isbn("isbn")
                        .image("image")
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1자 이상 입력해야 하는 정보입니다");
    }

    @Test
    @DisplayName("도서 저자가 비어있으면 예외가 발생한다")
    void validateEmpty_tooShortAuthor_fail() {
        // given
        final String author = "";

        // when - then
        assertThatThrownBy(
                () -> Book.builder()
                        .title("title")
                        .summary("summary")
                        .author(author)
                        .publisher("publisher")
                        .isbn("isbn")
                        .image("image")
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1자 이상 입력해야 하는 정보입니다");
    }

    @Test
    @DisplayName("도서 출판사가 비어있으면 예외가 발생한다")
    void validateEmpty_tooShortPublisher_fail() {
        // given
        final String publisher = "";

        // when - then
        assertThatThrownBy(
                () -> Book.builder()
                        .title("title")
                        .summary("summary")
                        .author("author")
                        .publisher(publisher)
                        .isbn("isbn")
                        .image("image")
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1자 이상 입력해야 하는 정보입니다");
    }

    @Test
    @DisplayName("도서 isbn이 비어있으면 예외가 발생한다")
    void validateEmpty_tooShortIsbn_fail() {
        // given
        final String isbn = "";

        // when - then
        assertThatThrownBy(
                () -> Book.builder()
                        .title("title")
                        .summary("summary")
                        .author("author")
                        .publisher("publisher")
                        .isbn(isbn)
                        .image("image")
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISBN은 13자여야 합니다");
    }

    @Test
    @DisplayName("도서 정보를 변경한다")
    void update() {
        // given
        final Book book = Book.builder()
                .title("오브젝트")
                .summary("오브젝트 설명")
                .author("조영호")
                .publisher("인사이트")
                .isbn("1234567890123")
                .image("image.png")
                .build();

        final String updatedTitle = "오브젝트2";
        final String updatedSummary = "오브젝트 설명2";
        final String updatedAuthor = "조영호2";
        final String updatedPublisher = "인사이트2";
        final String updatedIsbn = "1234567890000";
        final String updatedImage = "image2.png";

        // when
        book.update(updatedTitle, updatedSummary, updatedAuthor, updatedPublisher, updatedIsbn, updatedImage);

        // then
        assertAll(
                () -> assertThat(book.getTitle()).isEqualTo(updatedTitle),
                () -> assertThat(book.getSummary()).isEqualTo(updatedSummary),
                () -> assertThat(book.getAuthor()).isEqualTo(updatedAuthor),
                () -> assertThat(book.getPublisher()).isEqualTo(updatedPublisher),
                () -> assertThat(book.getIsbn()).isEqualTo(updatedIsbn),
                () -> assertThat(book.getImage()).isEqualTo(updatedImage)
        );
    }

    @Test
    @DisplayName("변경된 도서 정보만 변경한다")
    void update_onlyChangedField() {
        // given
        final String title = "오브젝트";
        final String author = "조영호";
        final String isbn = "1234567890123";

        final Book book = Book.builder()
                .title(title)
                .summary("오브젝트 설명")
                .author(author)
                .publisher("인사이트")
                .isbn(isbn)
                .image("image.png")
                .build();

        final String updatedSummary = "오브젝트 설명2";
        final String updatedPublisher = "인사이트2";
        final String updatedImage = "image2.png";

        // when
        book.update(title, updatedSummary, author, updatedPublisher, isbn, updatedImage);

        // then
        assertAll(
                () -> assertThat(book.getTitle()).isEqualTo(title),
                () -> assertThat(book.getSummary()).isEqualTo(updatedSummary),
                () -> assertThat(book.getAuthor()).isEqualTo(author),
                () -> assertThat(book.getPublisher()).isEqualTo(updatedPublisher),
                () -> assertThat(book.getIsbn()).isEqualTo(isbn),
                () -> assertThat(book.getImage()).isEqualTo(updatedImage)
        );
    }
}
