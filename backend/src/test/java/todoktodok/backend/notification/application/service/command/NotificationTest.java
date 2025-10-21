package todoktodok.backend.notification.application.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.application.service.command.CommentCommandService;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Disabled
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class NotificationTest {

    @MockitoBean
    private FcmPushNotifier fcmPushNotifier;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private CommentCommandService commentCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("댓글을 작성하면 댓글 알림이 토론방 작성자에게 가는 이벤트가 발생한다.")
    void commentCreatedNotificationTest() {
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setUserInfo("user1@gmail.com", "user1", "", "");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");
        databaseInitializer.setDiscussionInfo("캡슐화 왜 하나요?", "진짜 왜해요? 의견 공유!", 1L, 1L);

        commentCommandService.createComment(2L, 1L, new CommentRequest("저도 궁금합니다"));

        verify(fcmPushNotifier, times(1)).sendPush(any(), any());
    }
}
