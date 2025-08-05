package todoktodok.backend.reply.application.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.reply.application.dto.request.ReplyRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class ReplyCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private ReplyCommandService replyCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("없는 토론방에 대해 대댓글을 작성할 경우 예외가 발생한다")
    void createReplyTest_discussionNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        // when - then
        assertThatThrownBy(() -> replyCommandService.createReply(1L, 1L, 1L, replyRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 토론방을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("없는 댓글에 대해 대댓글을 작성할 경우 예외가 발생한다")
    void createReplyTest_commentNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        // when - then
        assertThatThrownBy(() -> replyCommandService.createReply(1L, 1L, 1L, replyRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 댓글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 대댓글을 작성할 경우 예외가 발생한다")
    void createReplyTest_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        // when - then
        assertThatThrownBy(() -> replyCommandService.createReply(2L, 1L, 1L, replyRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("자기 자신을 신고하면 예외가 발생한다")
    void validateSelfReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final Long replyId = 1L;

        // when - then
        assertThatThrownBy(() -> replyCommandService.report(memberId, discussionId, commentId, replyId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신이 작성한 대댓글을 신고할 수 없습니다");
    }

    @Test
    @DisplayName("없는 대댓글을 신고하면 예외가 발생한다")
    void validateNotExistReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final Long replyId = 2L;

        // when - then
        assertThatThrownBy(() -> replyCommandService.report(memberId, discussionId, commentId, replyId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 대댓글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("이미 자신이 신고한 대댓글을 중복 신고하면 예외가 발생한다")
    void validateDuplicatedReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setReplyInfo("저도 같은 의견입니다!", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final Long replyId = 1L;

        replyCommandService.report(memberId, discussionId, commentId, replyId);

        // when - then
        assertThatThrownBy(() -> replyCommandService.report(memberId, discussionId, commentId, replyId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신고한 대댓글입니다");
    }

    @Test
    @DisplayName("자신의 것이 아닌 대댓글을 수정하면 예외가 발생한다")
    void validateReplyMemberUpdateTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");
        databaseInitializer.setReplyInfo("member1L의 대댓글입니다", 1L, 1L);
        databaseInitializer.setReplyInfo("member2L의 대댓글입니다", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final Long replyId = 2L;
        final ReplyRequest replyRequest = new ReplyRequest(
                "member2L의 대댓글입니다"
        );

        // when - then
        assertThatThrownBy(
                () -> replyCommandService.updateReply(memberId, discussionId, commentId, replyId, replyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신의 대댓글만 수정/삭제 가능합니다");
    }

    @Test
    @DisplayName("수정하는 댓글과 토론방이 일치하지 않으면 예외가 발생한다")
    void validateDiscussionCommentUpdateTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDiscussionInfo("오브젝트", "오브젝트 토론입니다", 1L, 1L);

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 2L;
        final Long commentId = 1L;
        final Long replyId = 1L;
        final ReplyRequest replyRequest = new ReplyRequest(
                "comment1L의 대댓글입니다"
        );

        // when - then
        assertThatThrownBy(
                () -> replyCommandService.updateReply(memberId, discussionId, commentId, replyId, replyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("수정하는 대댓글과 댓글이 일치하지 않으면 예외가 발생한다")
    void validateReplyCommentUpdateTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 2L;
        final Long replyId = 1L;
        final ReplyRequest replyRequest = new ReplyRequest(
                "comment1L의 대댓글입니다"
        );

        // when - then
        assertThatThrownBy(
                () -> replyCommandService.updateReply(memberId, discussionId, commentId, replyId, replyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 댓글에 있는 대댓글이 아닙니다");
    }

    @Test
    @DisplayName("자신의 것이 아닌 대댓글을 삭제하면 예외가 발생한다")
    void validateReplyMemberDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");
        databaseInitializer.setReplyInfo("저는 다르게 생각합니다!", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final Long replyId = 2L;

        // when - then
        assertThatThrownBy(() -> replyCommandService.deleteReply(memberId, discussionId, commentId, replyId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신의 대댓글만 수정/삭제 가능합니다");
    }

    @Test
    @DisplayName("삭제하는 댓글과 토론방이 일치하지 않으면 예외가 발생한다")
    void validateDiscussionCommentDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDiscussionInfo("오브젝트", "오브젝트 토론입니다", 1L, 1L);

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 2L;
        final Long commentId = 1L;
        final Long replyId = 1L;

        // when - then
        assertThatThrownBy(() -> replyCommandService.deleteReply(memberId, discussionId, commentId, replyId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("삭제하는 대댓글과 댓글이 일치하지 않으면 예외가 발생한다")
    void validateReplyCommentDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 2L;
        final Long replyId = 1L;

        // when - then
        assertThatThrownBy(() -> replyCommandService.deleteReply(memberId, discussionId, commentId, replyId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 댓글에 있는 대댓글이 아닙니다");
    }
}
