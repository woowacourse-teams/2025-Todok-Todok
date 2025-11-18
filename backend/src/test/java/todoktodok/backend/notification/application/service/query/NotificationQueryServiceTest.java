package todoktodok.backend.notification.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.application.dto.response.UnreadNotificationResponse;

@Disabled
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class NotificationQueryServiceTest {

    @Autowired
    private NotificationQueryService notificationQueryService;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("사용자는 자신의 알림 목록과 읽지 않은 알림 수를 조회할 수 있다")
    void getNotifications_success() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();
        databaseInitializer.setDefaultReplyNotification();

        // when
        final NotificationResponse response = notificationQueryService.getNotifications(1L);

        // then
        assertThat(response.unreadCount()).isEqualTo(2);
        assertThat(response.notifications()).hasSize(2);
        assertThat(response.notifications().get(0).data().memberNickname()).isEqualTo("user");
    }

    @Test
    @DisplayName("읽지 않은 알림이 있으면 true를 반환한다")
    void checkUnReadNotification_true() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        // when
        final UnreadNotificationResponse response = notificationQueryService.hasUnreadNotifications(1L);

        // then
        assertThat(response.exist()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 조회하면 예외가 발생한다")
    void getNotifications_fail_noSuchMember() {
        // given
        final Long notExistMemberId = 999L;

        // when & then
        assertThatThrownBy(() -> notificationQueryService.getNotifications(notExistMemberId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }
}
