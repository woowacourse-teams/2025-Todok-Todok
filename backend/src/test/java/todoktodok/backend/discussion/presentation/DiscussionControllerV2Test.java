package todoktodok.backend.discussion.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import todoktodok.backend.discussion.application.dto.request.DiscussionRequestV2;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
public class DiscussionControllerV2Test {

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
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final String token = MemberFixture.login("user@gmail.com");

        final DiscussionRequestV2 discussionRequestV2 = new DiscussionRequestV2(
                1L,
                "이 책의 의존성 주입 방식에 대한 생각",
                "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
        );

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(discussionRequestV2)
                .when().post("/api/v2/discussions")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("토론방을 필터링한다")
    void filterDiscussions() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDiscussionInfo(
                "오브젝트", "오브젝트 토론입니다", 1L, 1L, null
        );

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v2/discussions?keyword=오브젝트&type=ALL")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Nested
    @DisplayName("토론방 필터링 실패 테스트")
    class FilterDiscussionsFailTest {

        @Test
        @DisplayName("토론방을 필터링할 때 type을 명시하지 않으면 예외가 발생한다")
        void fail_filterDiscussions_noType() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();
            databaseInitializer.setDiscussionInfo(
                    "오브젝트", "오브젝트 토론입니다", 1L, 1L, null
            );

            final String token = MemberFixture.login("user@gmail.com");
            final String uri = "/api/v2/discussions?keyword=오브젝트";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .when().get(uri)
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("토론방을 필터링할 때 type에 정해지지 않는 값을 추가하면 예외가 발생한다")
        void fail_filterDiscussions_invalidType() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();
            databaseInitializer.setDiscussionInfo(
                    "오브젝트", "오브젝트 토론입니다", 1L, 1L, null
            );

            final String token = MemberFixture.login("user@gmail.com");
            final String uri = "/api/v2/discussions?keyword=오브젝트&type=HELLO";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .when().get(uri)
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
