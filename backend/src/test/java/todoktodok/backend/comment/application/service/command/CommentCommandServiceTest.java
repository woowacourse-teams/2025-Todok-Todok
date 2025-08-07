package todoktodok.backend.comment.application.service.command;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import todoktodok.backend.comment.application.dto.request.CommentRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class CommentCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private CommentCommandService commentCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("없는 토론방에 대해 댓글을 작성할 경우 예외가 발생한다")
    void createCommentTest_discussionNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final CommentRequest commentRequest = new CommentRequest("상속의 핵심 목적은 타입 계층의 구축입니다!");

        // when - then
        assertThatThrownBy(() -> commentCommandService.createComment(1L, 1L, commentRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 토론방을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 댓글을 작성할 경우 예외가 발생한다")
    void createCommentTest_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final CommentRequest commentRequest = new CommentRequest("상속의 핵심 목적은 타입 계층의 구축입니다!");

        // when - then
        assertThatThrownBy(() -> commentCommandService.createComment(2L, 1L, commentRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("좋아요를 누르지 않았던 댓글에 좋아요를 생성한다")
    void commentToggleLikeTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final boolean isLiked = commentCommandService.toggleLike(memberId, discussionId, commentId);

        // then
        assertThat(isLiked).isTrue();
    }

    @Test
    @DisplayName("이미 좋아요를 누른 댓글에 다시 좋아요를 누르면 좋아요가 취소된다")
    void commentToggleLikeDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setCommentLikeInfo(1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final boolean isLiked = commentCommandService.toggleLike(memberId, discussionId, commentId);

        // then
        assertThat(isLiked).isFalse();
    }

    @Test
    @DisplayName("좋아요를 생성하는 댓글과 토론방이 일치하지 않으면 예외가 발생한다")
    void validateDiscussionCommentToggleLikeTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDiscussionInfo("오브젝트", "오브젝트 토론입니다", 1L, 1L);

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 2L;
        final Long commentId = 1L;

        // when - then
        assertThatThrownBy(() -> commentCommandService.toggleLike(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("자기 자신을 신고하면 예외가 발생한다")
    void validateSelfReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when - then
        assertThatThrownBy(() -> commentCommandService.report(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신이 작성한 댓글을 신고할 수 없습니다");
    }

    @Test
    @DisplayName("이미 자신이 신고한 댓글을 중복 신고하면 예외가 발생한다")
    void validateDuplicatedReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        commentCommandService.report(memberId, discussionId, commentId);

        // when - then
        assertThatThrownBy(() -> commentCommandService.report(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신고한 댓글입니다");
    }

    @Test
    @DisplayName("자신의 것이 아닌 댓글을 수정하면 예외가 발생한다")
    void validateCommentMemberUpdateTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");
        databaseInitializer.setCommentInfo("member 2L의 댓글입니다", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final CommentRequest commentRequest = new CommentRequest(
                "member1L의 댓글입니다"
        );

        // when - then
        assertThatThrownBy(() -> commentCommandService.updateComment(memberId, discussionId, commentId, commentRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신의 댓글만 수정/삭제 가능합니다");
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

        final Long memberId = 1L;
        final Long discussionId = 2L;
        final Long commentId = 1L;
        final CommentRequest commentRequest = new CommentRequest(
                "discussion 1L의 댓글입니다"
        );

        // when - then
        assertThatThrownBy(() -> commentCommandService.updateComment(memberId, discussionId, commentId, commentRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("자신의 것이 아닌 댓글을 삭제하면 예외가 발생한다")
    void validateCommentMemberDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");
        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when - then
        assertThatThrownBy(() -> commentCommandService.deleteComment(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신의 댓글만 수정/삭제 가능합니다");
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

        final Long memberId = 1L;
        final Long discussionId = 2L;
        final Long commentId = 1L;

        // when - then
        assertThatThrownBy(() -> commentCommandService.deleteComment(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("대댓글이 존재하는 댓글을 삭제하면 예외가 발생한다")
    void validateHasCommentDiscussionDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when - then
        assertThatThrownBy(() -> commentCommandService.deleteComment(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대댓글이 존재하는 댓글은 삭제할 수 없습니다");
    }
}
