package todoktodok.backend.notification.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.notification.application.dto.request.NotificationTokenRequest;
import todoktodok.backend.notification.domain.repository.NotificationTokenRepository;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class NotificationTokenCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private NotificationTokenCommandService notificationTokenCommandService;

    @Autowired
    private NotificationTokenRepository notificationTokenRepository;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("중복된 FCM 토큰 저장 시 예외가 발생한다")
    void saveNotificationTokenTest_duplicatedToken_fail() {
        // give
        final String token = "fcmRegistrationToken";
        final String fid = "Firebase_Installation_Id";
        final Long memberId = 1L;

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setNotificationTokenInfo(token, fid, memberId);

        // when - then
        assertThatThrownBy(() -> notificationTokenCommandService.save(memberId, new NotificationTokenRequest(token, fid)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 토큰은 중복된 토큰입니다");
    }

    @Test
    @DisplayName("NotificationToken 저장 시 이미 존재하는 동일한 fid의 토큰을 삭제한다")
    void saveNotificationTokenTest_deleteByFid() {
        // give
        final String oldToken = "oldFcmRegistrationToken";
        final String newToken = "newFcmRegistrationToken";
        final String fid = "Firebase_Installation_Id";
        final Long memberId = 1L;

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setNotificationTokenInfo(oldToken, fid, memberId);

        // when
        notificationTokenCommandService.save(memberId, new NotificationTokenRequest(newToken, fid));

       final boolean isOldTokenExists = notificationTokenRepository.existsByToken(oldToken);

        // then
        assertThat(isOldTokenExists).isFalse();
    }

}
