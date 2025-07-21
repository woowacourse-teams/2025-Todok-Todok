package todoktodok.backend.discussion.presentation;

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
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
class DiscussionControllerTest {

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
    @DisplayName("토론방을 생성한다")
    void createDiscussion() {
        // given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        databaseInitializer.setShelfInfo();
        databaseInitializer.setNoteInfo();

        final String token = MemberFixture.login("user@gmail.com");

        final DiscussionRequest discussionRequest = new DiscussionRequest(
                1L,
                "이 책의 의존성 주입 방식에 대한 생각",
                "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
        );

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(discussionRequest)
                .when().post("/api/v1/discussions")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("전체 토론방을 조회한다")
    void getDiscussions() {
        // given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        databaseInitializer.setShelfInfo();
        databaseInitializer.setNoteInfo();
        databaseInitializer.setDiscussionInfo();

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/v1/discussions")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
