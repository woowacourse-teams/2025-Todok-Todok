package todoktodok.backend.discussion.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.note.domain.Note;

class DiscussionTest {

    @Test
    @DisplayName("토론방 제목이 비어있으면 예외가 발생한다")
    void validateTitle_tooShortTitle_fail() {
        // given
        final Member member = getMember();
        final Book book = getBook();
        final Note note = getNote(book, member);

        final String title = "";

        // when - then
        assertThatThrownBy(
                        () -> Discussion.builder()
                                .title(title)
                                .content("클린한 코드를 작성해봅시다")
                                .member(member)
                                .book(book)
                                .note(note)
                                .build()
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토론방 제목은 1자 이상, 50자 이하여야 합니다.");
    }

    @Test
    @DisplayName("토론방 제목이 50자를 초과하면 예외가 발생한다")
    void validateTitle_tooLongTitle_fail() {
        // given
        final Member member = getMember();
        final Book book = getBook();
        final Note note = getNote(book, member);

        final String title = "a".repeat(51);

        // when - then
        assertThatThrownBy(
                        () -> Discussion.builder()
                                .title(title)
                                .content("클린한 코드를 작성해봅시다")
                                .member(member)
                                .book(book)
                                .note(note)
                                .build()
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토론방 제목은 1자 이상, 50자 이하여야 합니다.");
    }

    @Test
    @DisplayName("토론방 내용이 2500자를 초과하면 예외가 발생한다.")
    void validateContent_tooLongContent_fail() {
        // given
        final Member member = getMember();
        final Book book = getBook();
        final Note note = getNote(book, member);

        final String content = "a".repeat(2501);

        // when - then
        assertThatThrownBy(
                        () -> Discussion.builder()
                                .title("클린코드")
                                .content(content)
                                .member(member)
                                .book(book)
                                .note(note)
                                .build()
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토론방 내용은 1자 이상, 2500자 이하여야 합니다.");
    }

    @Test
    @DisplayName("토론방 내용이 비어 있으면 예외가 발생한다")
    void validateContent_TooShortContent_fail() {
        // given
        final Member member = getMember();
        final Book book = getBook();
        final Note note = getNote(book, member);

        final String content = "";

        // when - then
        assertThatThrownBy(
                        () -> Discussion.builder()
                                .title("클린코드")
                                .content(content)
                                .member(member)
                                .book(book)
                                .note(note)
                                .build()
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토론방 내용은 1자 이상, 2500자 이하여야 합니다.");
    }

    private Note getNote(Book book, Member member) {
        return Note.builder()
                .snap("클린코드")
                .memo("로버트마틴")
                .book(book)
                .member(member)
                .build();
    }

    private Book getBook() {
        return Book.builder()
                .title("클린코드")
                .author("로버트마틴")
                .publisher("피어슨")
                .isbn("123")
                .build();
    }

    private Member getMember() {
        return Member.builder()
                .email("user@gmail.com")
                .nickname("user")
                .profileImage("https://image.jpg")
                .build();
    }
}
