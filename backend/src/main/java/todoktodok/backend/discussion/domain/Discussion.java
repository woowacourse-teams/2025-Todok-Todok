package todoktodok.backend.discussion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.global.common.TimeStamp;
import todoktodok.backend.member.domain.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE discussion SET deleted_at = NOW() WHERE id = ?")
public class Discussion extends TimeStamp {

    public static final int TITLE_MAX_LENGTH = 50;
    public static final int CONTENT_MAX_LENGTH = 2500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2550)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Book book;

    @Builder
    public static Discussion create(
            final String title,
            final String content,
            final Member member,
            final Book book
    ) {
        validateTitle(title);
        validateContent(content);

        return new Discussion(null, title, content, member, book);
    }

    public boolean isOwnedBy(final Member member) {
        return this.member.equals(member);
    }

    public void update(final String title, final String content) {
        validateTitle(title);
        validateContent(content);

        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        Class<?> thisClass = org.hibernate.Hibernate.getClass(this);
        Class<?> thatClass = org.hibernate.Hibernate.getClass(o);
        if (thisClass != thatClass) {
            return false;
        }

        Discussion that = (Discussion) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private static void validateTitle(final String title) {
        if (title.isEmpty() || title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("토론방 제목은 1자 이상, 50자 이하여야 합니다");
        }
    }

    private static void validateContent(final String content) {
        if (content.isEmpty() || content.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException("토론방 내용은 1자 이상, 2500자 이하여야 합니다");
        }
    }
}
