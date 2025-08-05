package todoktodok.backend.reply.presentation;

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
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.member.presentation.fixture.MemberFixture;
import todoktodok.backend.reply.application.dto.request.ReplyRequest;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
public class ReplyControllerTest {

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
    @DisplayName("대댓글을 생성한다")
    void createReplyTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(replyRequest)
                .when().post("/api/v1/discussions/1/comments/1/replies")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("대댓글을 신고한다")
    void reportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setReplyInfo("저도 같은 의견입니다!", 2L, 1L);

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/discussions/1/comments/1/replies/1/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("대댓글을 수정한다")
    void updateReplyTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setReplyInfo("저도 같은 의견입니다!", 1L, 1L);

        final String updatedContent = "아니다, 생각해보니 상속의 핵심 목적은 아니네요.";
        final ReplyRequest replyRequest = new ReplyRequest(
                updatedContent
        );

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(replyRequest)
                .when().patch("/api/v1/discussions/1/comments/1/replies/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("대댓글을 삭제한다")
    void deleteReplyTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().delete("/api/v1/discussions/1/comments/1/replies/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
