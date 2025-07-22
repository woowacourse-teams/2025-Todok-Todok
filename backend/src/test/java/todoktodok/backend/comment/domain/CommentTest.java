package todoktodok.backend.comment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.presentation.fixture.BookFixture;
import todoktodok.backend.comment.domain.fixture.CommentFixture;
import todoktodok.backend.discussion.domain.fixture.DiscussionFixture;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.presentation.fixture.MemberFixture;
import todoktodok.backend.note.domain.Note;
import todoktodok.backend.note.presentation.fixture.NoteFixture;

public class CommentTest {

    @Test
    @DisplayName("댓글 내용이 비어있으면 예외가 발생한다")
    void validateContent_isEmpty_fail() {
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
                "123"
        );

        final Note note = NoteFixture.create(
                "코드는 다른 사람이 읽을 수도 있다는 사실을 항상 염두에 두어야 한다.",
                "함수명 하나 짓는 데 시간을 들이는 이유가 명확해졌다.",
                book,
                member
        );

        final Discussion discussion = DiscussionFixture.create(
                "클린코드",
                "네이밍은 언제나 중요하다",
                member,
                book,
                note
        );

        // when - then
        assertThatThrownBy(
                () -> Comment.builder()
                        .content(content)
                        .member(member)
                        .discussion(discussion)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글 내용은 1자 이상, 1500자 이하여야 합니다");
    }

    @Test
    @DisplayName("댓글 내용이 1500자를 초과하면 예외가 발생한다")
    void validateContent_tooLongContent_fail() {
        // given
        final String content = "a".repeat(1501);

        final Member member = MemberFixture.create(
                "user@gmail.com",
                "user",
                "https://image.jpg"
        );

        final Book book = BookFixture.create(
                "클린코드",
                "로버트마틴",
                "피어슨",
                "123"
        );

        final Note note = NoteFixture.create(
                "코드는 다른 사람이 읽을 수도 있다는 사실을 항상 염두에 두어야 한다.",
                "함수명 하나 짓는 데 시간을 들이는 이유가 명확해졌다.",
                book,
                member
        );

        final Discussion discussion = DiscussionFixture.create(
                "클린코드",
                "네이밍은 언제나 중요하다",
                member,
                book,
                note
        );

        // when - then
        assertThatThrownBy(
                () -> Comment.builder()
                        .content(content)
                        .member(member)
                        .discussion(discussion)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글 내용은 1자 이상, 1500자 이하여야 합니다");
    }

    @Test
    @DisplayName("해당 토론방에 있는 댓글이 아니면 예외가 발생한다")
    void validateComment_isExistInDiscuss_fail() {
        // given
        final Member member = MemberFixture.create(
                "user@gmail.com",
                "user",
                "https://image.jpg"
        );

        final Book book = BookFixture.create(
                "클린코드",
                "로버트마틴",
                "피어슨",
                "123"
        );

        final Note note = NoteFixture.create(
                "코드는 다른 사람이 읽을 수도 있다는 사실을 항상 염두에 두어야 한다.",
                "함수명 하나 짓는 데 시간을 들이는 이유가 명확해졌다.",
                book,
                member
        );

        final Discussion discussion = DiscussionFixture.create(
                "클린코드",
                "네이밍은 언제나 중요하다",
                member,
                book,
                note
        );

        final Comment comment = CommentFixture.create(
                "네이밍에 너무 많은 시간을 쓸 필요가 있을까요?",
                member,
                discussion
        );

        final Discussion discussion2 = DiscussionFixture.create(
                "오브젝트",
                "상속보다 합성을 사용하자",
                member,
                book,
                note
        );

        // when - then
        assertThatThrownBy(
                () -> comment.validateMatchWithDiscussion(discussion2)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론방에 있는 댓글이 아닙니다");
    }
}
