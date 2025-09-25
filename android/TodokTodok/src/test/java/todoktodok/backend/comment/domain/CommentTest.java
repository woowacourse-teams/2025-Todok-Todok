package todoktodok.backend.comment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.presentation.fixture.BookFixture;
import todoktodok.backend.comment.domain.fixture.CommentFixture;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.fixture.DiscussionFixture;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

public class CommentTest {

    private Member member;
    private Book book;
    private Discussion discussion;

    @BeforeEach
    void setUp() {
        member = MemberFixture.create("user@gmail.com", "user", "https://image.jpg");
        book = BookFixture.create("클린코드", "로버트마틴", "피어슨", "1234567890123");
        discussion = DiscussionFixture.create("클린코드", "네이밍은 언제나 중요하다", member, book);
    }

    @Test
    @DisplayName("댓글 내용이 비어있으면 예외가 발생한다")
    void validateContent_isEmpty_fail() {
        // given
        final String content = "";

        // when - then
        assertThatThrownBy(
                () -> Comment.builder()
                        .content(content)
                        .member(member)
                        .discussion(discussion)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("댓글 내용은 1자 이상, 2000자 이하여야 합니다");
    }

    @Test
    @DisplayName("댓글 내용이 2000자를 초과하면 예외가 발생한다")
    void validateContent_tooLongContent_fail() {
        // given
        final String content = "a".repeat(2001);

        // when - then
        assertThatThrownBy(
                () -> Comment.builder()
                        .content(content)
                        .member(member)
                        .discussion(discussion)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("댓글 내용은 1자 이상, 2000자 이하여야 합니다");
    }

    @Test
    @DisplayName("해당 토론방에 있는 댓글이 아니면 예외가 발생한다")
    void validateComment_isExistInDiscuss_fail() {
        // given
        final Comment comment = CommentFixture.create(
                "네이밍에 너무 많은 시간을 쓸 필요가 있을까요?",
                member,
                discussion
        );

        final Discussion anotherDiscussion = DiscussionFixture.create(
                "오브젝트",
                "상속보다 합성을 사용하자",
                member,
                book
        );

        // when - then
        assertThatThrownBy(() -> comment.validateMatchWithDiscussion(anotherDiscussion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("자기 자신이 작성한 댓글을 신고하려 하면 예외가 발생한다")
    void validateComment_report_fail() {
        // given
        final Comment comment = CommentFixture.create(
                "네이밍에 너무 많은 시간을 쓸 필요가 있을까요?",
                member,
                discussion
        );

        // when - then
        assertThatThrownBy(() -> comment.validateSelfReport(member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신이 작성한 댓글을 신고할 수 없습니다");
    }
}
