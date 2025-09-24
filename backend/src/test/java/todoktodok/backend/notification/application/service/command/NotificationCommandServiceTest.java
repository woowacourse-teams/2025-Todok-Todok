package todoktodok.backend.notification.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.notification.domain.Notification;
import todoktodok.backend.notification.domain.NotificationTarget;
import todoktodok.backend.notification.domain.NotificationType;
import todoktodok.backend.notification.domain.repository.NotificationRepository;
import todoktodok.backend.notification.exception.NotificationForbiddenException;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class NotificationCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private NotificationCommandService notificationCommandService;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("알림을 저장한다")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void createNotification() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        final Long recipientId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;
        final Long replyId = 1L;
        final String memberNickname = "사용자";
        final String discussionTitle = "토론방 제목입니다";
        final String content = "토론방 내용입니다";
        final NotificationType type = NotificationType.COMMENT;
        final NotificationTarget target = NotificationTarget.DISCUSSION;

        final CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest(
                recipientId,
                discussionId,
                commentId,
                replyId,
                memberNickname,
                discussionTitle,
                content,
                type,
                target
        );

        // when
        final Long notificationId = notificationCommandService.createNotification(createNotificationRequest);

        // then
        assertThat(notificationId).isEqualTo(1L);
    }

    @Test
    @DisplayName("사용자는 자신의 알림을 읽음 처리할 수 있다")
    void markNotificationAsRead_success() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        // when
        notificationCommandService.markNotificationAsRead(1L, 1L);
        final Notification notification = notificationRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림은 존재하지 않습니다"));

        // then
        assertThat(notification.isRead()).isTrue();
    }

    @Test
    @DisplayName("다른 사용자의 알림을 읽음 처리하려 하면 예외가 발생한다")
    void markNotificationAsRead_fail_notOwner() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("email2@naver.com", "user2", "image.svg", "안녕하세요");
        databaseInitializer.setDefaultCommentNotification();

        // when & then
        assertThatThrownBy(() -> notificationCommandService.markNotificationAsRead(2L, 1L))
                .isInstanceOf(NotificationForbiddenException.class)
                .hasMessageContaining("본인 알림이 아닙니다");
    }

    @Test
    @DisplayName("존재하지 않는 알림 ID로 알림 읽음 요청하면 예외가 발생한다")
    void markNotificationAsRead_fail_notFound() {
        // given
        databaseInitializer.setDefaultUserInfo();
        final Long nonExistsNotificationId = 999L;

        // when & then
        assertThatThrownBy(() -> notificationCommandService.markNotificationAsRead(1L, nonExistsNotificationId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 알림을 찾을 수 없습니다 : notificationId = " + nonExistsNotificationId);
    }

    @Test
    @DisplayName("사용자는 자신의 알림을 삭제할 수 있다")
    void deleteNotification_success() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        final Long memberId = 1L;
        final Long notificationId = 1L;

        // when
        notificationCommandService.deleteNotification(memberId, notificationId);

        // then
        assertThat(notificationRepository.findById(notificationId)).isEmpty();
    }

    @Test
    @DisplayName("다른 사용자의 알림을 삭제하려 하면 예외가 발생한다")
    void deleteNotification_fail_notOwner() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("other@test.com", "다른유저", "image.svg", "msg");
        databaseInitializer.setDefaultCommentNotification();

        final Long otherMemberId = 2L;
        final Long notificationId = 1L;

        // when & then
        assertThatThrownBy(() -> notificationCommandService.deleteNotification(otherMemberId, notificationId))
                .isInstanceOf(NotificationForbiddenException.class) // 실제 서비스에서 쓰는 예외 타입으로 교체
                .hasMessageContaining("본인 알림이 아닙니다");
    }

    @Test
    @DisplayName("존재하지 않는 알림을 삭제하려 하면 예외가 발생한다")
    void deleteNotification_fail_notFound() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final Long memberId = 1L;
        final Long nonExistsNotificationId = 999L;

        // when & then
        assertThatThrownBy(() -> notificationCommandService.deleteNotification(memberId, nonExistsNotificationId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 알림을 찾을 수 없습니다 : notificationId = " + nonExistsNotificationId);
    }
}
