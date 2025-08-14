package todoktodok.backend.discussion.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.presentation.fixture.BookFixture;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

class DiscussionTest {

    @Test
    @DisplayName("토론방 제목이 비어있으면 예외가 발생한다")
    void validateTitle_tooShortTitle_fail() {
        // given
        final String title = "";

        final Member member = MemberFixture.create(
                "user@gmail.com",
                "user",
                "https://image.jpg"
        );

        final Book book = BookFixture.create(
                "클린코드",
                "로버트마틴",
                "피어슨",
                "1234567890123"
        );

        // when - then
        assertThatThrownBy(
                () -> Discussion.builder()
                        .title(title)
                        .content("클린한 코드를 작성해봅시다")
                        .member(member)
                        .book(book)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토론방 제목은 1자 이상, 50자 이하여야 합니다");
    }

    @Test
    @DisplayName("토론방 제목이 50자를 초과하면 예외가 발생한다")
    void validateTitle_tooLongTitle_fail() {
        // given
        final String title = "a".repeat(51);

        final Member member = MemberFixture.create(
                "user@gmail.com",
                "user",
                "https://image.jpg"
        );

        final Book book = BookFixture.create(
                "클린코드",
                "로버트마틴",
                "피어슨",
                "1234567890123"
        );

        // when - then
        assertThatThrownBy(
                () -> Discussion.builder()
                        .title(title)
                        .content("클린한 코드를 작성해봅시다")
                        .member(member)
                        .book(book)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토론방 제목은 1자 이상, 50자 이하여야 합니다");
    }

    @Test
    @DisplayName("토론방 내용이 2500자를 초과하면 예외가 발생한다.")
    void validateContent_tooLongContent_fail() {
        // given
        final String content = "a".repeat(2501);

        final Member member = MemberFixture.create(
                "user@gmail.com",
                "user",
                "https://image.jpg"
        );

        final Book book = BookFixture.create(
                "클린코드",
                "로버트마틴",
                "피어슨",
                "1234567890123"
        );

        // when - then
        assertThatThrownBy(
                () -> Discussion.builder()
                        .title("클린코드")
                        .content(content)
                        .member(member)
                        .book(book)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토론방 내용은 1자 이상, 2500자 이하여야 합니다");
    }

    @Test
    @DisplayName("토론방 내용이 비어 있으면 예외가 발생한다")
    void validateContent_TooShortContent_fail() {
        // given
        final String content = "";

        final Member member = MemberFixture.create(
                "user@gmail.com",
                "user",
                "https://image.jpg"
        );

        final Book book = BookFixture.create(
                "클린코드",
                "로버트마틴",
                "피어슨",
                "1234567890123"
        );

        // when - then
        assertThatThrownBy(
                () -> Discussion.builder()
                        .title("클린코드")
                        .content(content)
                        .member(member)
                        .book(book)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("토론방 내용은 1자 이상, 2500자 이하여야 합니다");
    }
}
