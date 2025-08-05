package todoktodok.backend.reply.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.presentation.fixture.BookFixture;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.fixture.CommentFixture;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.fixture.DiscussionFixture;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.presentation.fixture.MemberFixture;
import todoktodok.backend.reply.domain.fixture.ReplyFixture;

public class ReplyTest {

    @Test
    @DisplayName("대댓글 내용이 비어있으면 예외가 발생한다")
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
                "1234567890123"
        );

        final Discussion discussion = DiscussionFixture.create(
                "클린코드",
                "네이밍은 언제나 중요하다",
                member,
                book
        );

        final Comment comment = CommentFixture.create(
                "네이밍에 너무 많은 시간을 쓸 필요가 있을까요?",
                member,
                discussion
        );

        // when - then
        assertThatThrownBy(
                () -> Reply.builder()
                        .content(content)
                        .member(member)
                        .comment(comment)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대댓글 내용은 1자 이상, 1500자 이하여야 합니다");
    }

    @Test
    @DisplayName("대댓글 내용이 1500자를 초과하면 예외가 발생한다")
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
                "1234567890123"
        );

        final Discussion discussion = DiscussionFixture.create(
                "클린코드",
                "네이밍은 언제나 중요하다",
                member,
                book
        );

        final Comment comment = CommentFixture.create(
                "네이밍에 너무 많은 시간을 쓸 필요가 있을까요?",
                member,
                discussion
        );

        // when - then
        assertThatThrownBy(
                () -> Reply.builder()
                        .content(content)
                        .member(member)
                        .comment(comment)
                        .build()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대댓글 내용은 1자 이상, 1500자 이하여야 합니다");
    }

    @Test
    @DisplayName("해당 댓글에 있는 대댓글이 아니면 예외가 발생한다")
    void validateReply_isExistInComment_fail() {
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
                "1234567890123"
        );

        final Discussion discussion = DiscussionFixture.create(
                "클린코드",
                "네이밍은 언제나 중요하다",
                member,
                book
        );

        final Discussion anotherDiscussion = DiscussionFixture.create(
                "오브젝트",
                "상속보다 합성을 사용하자",
                member,
                book
        );

        final Comment comment = CommentFixture.create(
                "네이밍에 너무 많은 시간을 쓸 필요가 있을까요?",
                member,
                discussion
        );

        final Comment anotherComment = CommentFixture.create(
                "상속과 합성은 뭐가 다른가요?",
                member,
                anotherDiscussion
        );

        final Reply reply = ReplyFixture.create(
                "저도 그 의견에 동의합니다!",
                member,
                comment
        );

        // when - then
        assertThatThrownBy(() -> reply.validateMatchWithComment(anotherComment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 댓글에 있는 대댓글이 아닙니다");
    }
}
