package todoktodok.backend.comment.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Disabled
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class CommentCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private CommentCommandService commentCommandService;

    @MockitoBean
    private FcmPushNotifier fcmPushNotifier;

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
                .hasMessageContaining("해당 토론방을 찾을 수 없습니다");
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
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("댓글을 작성하면 댓글 알이 토론방 작성자에게 가는 이벤트가 발생한다")
    void commentCreatedNotificationTest() {
        // given
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setUserInfo("user1@gmail.com", "user1", "", "");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");

        final Long recipientId = 1L;

        databaseInitializer.setDiscussionInfo("캡슐화 왜 하나요?", "진짜 왜해요? 의견 공유!", recipientId, 1L);

        final Long authorId = 2L;
        final Long discussionId = 1L;

        // when
        commentCommandService.createComment(authorId, discussionId, new CommentRequest("저도 궁금합니다"));

        // then
        verify(fcmPushNotifier, times(1)).sendPush(any(), any());
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
                .hasMessageContaining("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("댓글 좋아요를 생성하면 알이 댓글 작성자에게 가는 이벤트가 발생한다")
    void commentLikeCreatedNotificationTest() {
        // given
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setUserInfo("user1@gmail.com", "user1", "", "");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");
        databaseInitializer.setDefaultDiscussionInfo();

        final Long discussionId = 1L;
        final Long recipientId = 1L;

        databaseInitializer.setCommentInfo("저도 궁금해요", recipientId, discussionId);

        final Long authorId = 2L;
        final Long commentId = 1L;

        // when
        commentCommandService.toggleLike(authorId, discussionId, commentId);

        // then
        verify(fcmPushNotifier, times(1)).sendPush(any(), any());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("댓글 좋아요를 취소하면 알림이 댓글 작성자에게 가는 이벤트가 발생하지 않는다")
    void commentLikeCreatedNotificationTest_notCreated() {
        // given
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setUserInfo("user1@gmail.com", "user1", "", "");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");
        databaseInitializer.setDefaultDiscussionInfo();

        final Long discussionId = 1L;
        final Long recipientId = 1L;
        final Long authorId = 2L;
        final Long commentId = 1L;

        databaseInitializer.setCommentInfo("저도 궁금해요", recipientId, discussionId);
        databaseInitializer.setCommentLikeInfo(authorId, commentId);

        // when
        commentCommandService.toggleLike(authorId, discussionId, commentId);

        // then
        verifyNoInteractions(fcmPushNotifier);
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
        final String reason = "토론 주제와 무관한 내용";

        // when - then
        assertThatThrownBy(() -> commentCommandService.report(memberId, discussionId, commentId, reason))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신이 작성한 댓글을 신고할 수 없습니다");
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
        final String reason = "토론 주제와 무관한 내용";

        commentCommandService.report(memberId, discussionId, commentId, reason);

        // when - then
        assertThatThrownBy(() -> commentCommandService.report(memberId, discussionId, commentId, reason))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 신고한 댓글입니다");
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
                .hasMessageContaining("자기 자신의 댓글만 수정/삭제 가능합니다");
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
                .hasMessageContaining("해당 토론방에 있는 댓글이 아닙니다");
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
                .hasMessageContaining("자기 자신의 댓글만 수정/삭제 가능합니다");
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
                .hasMessageContaining("해당 토론방에 있는 댓글이 아닙니다");
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
                .hasMessageContaining("대댓글이 존재하는 댓글은 삭제할 수 없습니다");
    }
}
