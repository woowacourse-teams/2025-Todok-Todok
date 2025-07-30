package todoktodok.backend.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import todoktodok.backend.global.common.TimeStamp;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE book SET deleted_at = NOW() WHERE id = ?")
public class Book extends TimeStamp {

    public static final int ISBN_LENGTH = 13;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String summary;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, length = 13)
    private String isbn;

    private String image;

    @Builder
    public static Book create(
            final String title,
            final String summary,
            final String author,
            final String publisher,
            final String isbn,
            final String image
    ) {
        validateNotNullable(title, author, publisher, isbn);

        return new Book(null, title, summary, author, publisher, isbn, image);
    }

    private static void validateNotNullable(
            final String title,
            final String author,
            final String publisher,
            final String isbn
    ) {
        validateEmpty(title);
        validateEmpty(author);
        validateEmpty(publisher);
        validateIsbn(isbn);
    }

    private static void validateEmpty(final String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("1자 이상 입력해야 하는 정보입니다");
        }
    }

    private static void validateIsbn(final String isbn) {
        if (isbn == null || isbn.length() != ISBN_LENGTH) {
            throw new IllegalArgumentException("ISBN은 13자여야 합니다");
        }
    }

    public void update(
            final String title,
            final String summary,
            final String author,
            final String publisher,
            final String isbn,
            final String image
    ) {
        validateNotNullable(title, author, publisher, isbn);

        updateIfChanged(this.title, title, newValue -> this.title = newValue);
        updateIfChanged(this.summary, summary, newValue -> this.summary = newValue);
        updateIfChanged(this.author, author, newValue -> this.author = newValue);
        updateIfChanged(this.publisher, publisher, newValue -> this.publisher = newValue);
        updateIfChanged(this.isbn, isbn, newValue -> this.isbn = newValue);
        updateIfChanged(this.image, image, newValue -> this.image = newValue);
    }

    private <T> void updateIfChanged(
            final T oldValue,
            final T newValue,
            final Consumer<T> setter
    ) {
        if (!Objects.equals(oldValue, newValue)) {
            setter.accept(newValue);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> thisClass = org.hibernate.Hibernate.getClass(this);
        Class<?> thatClass = org.hibernate.Hibernate.getClass(o);
        if (thisClass != thatClass) return false;

        Book that = (Book) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
