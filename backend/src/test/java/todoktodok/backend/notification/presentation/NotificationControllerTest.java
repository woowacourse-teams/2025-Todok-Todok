package todoktodok.backend.notification.presentation;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.member.infrastructure.AuthClient;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
class NotificationControllerTest {

    private static final String DEFAULT_EMAIL = "user@gmail.com";

    @MockitoBean
    private AuthClient authClient;

    @Autowired
    private MemberFixture memberFixture;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("알림 목록을 조회한다")
    void getNotifications() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().get("/api/v1/notifications")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("unreadCount", equalTo(1))
                .body("notifications.size()", equalTo(1));
    }

    @Test
    @DisplayName("알림을 읽음 처리한다")
    void readNotification() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().patch("/api/v1/notifications/1/read")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 알림을 삭제한다")
    void deleteNotification() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().delete("/api/v1/notifications/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("안 읽은 알림 유무를 조회한다")
    void hasUnreadNotifications() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultCommentNotification();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().get("/api/v1/notifications/unread/exists")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("exist", equalTo(true));
    }
}
