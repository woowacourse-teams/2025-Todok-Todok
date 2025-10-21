package todoktodok.backend.comment.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import todoktodok.backend.comment.application.dto.request.CommentReportRequest;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.member.infrastructure.AuthClient;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@Disabled
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
public class CommentControllerTest {

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
    @DisplayName("댓글을 생성한다")
    void createCommentTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final CommentRequest commentRequest = new CommentRequest("상속의 핵심 목적은 타입 계층의 구축입니다!");

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(commentRequest)
                .when().post("/api/v1/discussions/1/comments")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("댓글 좋아요를 생성한다")
    void createCommentToggleLikeTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/discussions/1/comments/1/like")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("댓글 좋아요를 삭제한다")
    void deleteCommentToggleLikeTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setCommentLikeInfo(1L, 1L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/discussions/1/comments/1/like")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("댓글을 신고한다")
    void reportTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 2L, 1L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);
        final CommentReportRequest commentReportRequest = new CommentReportRequest("토론 주제와 무관한 내용");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(commentReportRequest)
                .when().post("/api/v1/discussions/1/comments/1/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("댓글을 단일 조회한다")
    void getCommentTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/discussions/1/comments/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("토론방별 댓글을 조회한다")
    void getCommentsTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);
        databaseInitializer.setCommentInfo("조합은 재사용이 목적입니다!", 1L, 1L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/discussions/1/comments")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @Test
    @DisplayName("댓글을 수정한다")
    void updateCommentTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);

        final String updatedContent = "아니다, 생각해보니 상속의 핵심 목적은 아니네요.";
        final CommentRequest commentRequest = new CommentRequest(
                updatedContent
        );

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(commentRequest)
                .when().patch("/api/v1/discussions/1/comments/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("댓글을 삭제한다")
    void deleteCommentTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 1L, 1L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().delete("/api/v1/discussions/1/comments/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
