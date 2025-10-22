package todoktodok.backend.discussion.presentation;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Duration;
import java.time.LocalDateTime;
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
import todoktodok.backend.discussion.application.dto.request.DiscussionReportRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
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
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

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
    @DisplayName("특정 토론방을 조회한다")
    void getDiscussion() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        await().atMost(Duration.ofSeconds(5))
                .pollDelay(Duration.ofMillis(200))
                .pollInterval(Duration.ofMillis(300))
                .untilAsserted(() -> RestAssured.given()
                        .header("Authorization", token)
                        .contentType(ContentType.JSON)
                        .when().get("/api/v1/discussions/1")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("viewCount", is(1)));
    }

    @Test
    @DisplayName("토론방을 최신순 조회한다 - 첫 페이지 조회")
    void getSlicedDiscussions_firstPage() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");
        final String cursorMeaningThree = "Mw==";

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().get("/api/v1/discussions?size=3")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(3))
                .body("pageInfo.hasNext", is(true))
                .body("pageInfo.nextCursor", is(cursorMeaningThree));
    }

    @Test
    @DisplayName("토론방을 최신순 조회한다 - 중간 페이지 조회")
    void getSlicedDiscussions_middlePage() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");
        final String cursorMeaningFive = "NQ==";
        final String cursorMeaningTwo = "Mg==";

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().get(String.format("/api/v1/discussions?size=3&cursor=%s", cursorMeaningFive))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(3))
                .body("pageInfo.hasNext", is(true))
                .body("pageInfo.nextCursor", is(cursorMeaningTwo));
    }

    @Test
    @DisplayName("토론방을 최신순 조회한다 - 마지막 페이지 조회")
    void getSlicedDiscussions_lastPage() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론방 제목", "토론방 내용", 1L, 1L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");
        final String cursorMeaningFour = "NA==";

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().get(String.format("/api/v1/discussions?size=3&cursor=%s", cursorMeaningFour))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", is(3))
                .body("pageInfo.hasNext", is(false))
                .body("pageInfo.nextCursor", nullValue());
    }

    @Test
    @DisplayName("토론방을 신고한다")
    void report() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user123@gmail.com", "user123", "https://image.png", "message");
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDiscussionInfo("토론방1", "토론방 내용", 2L, 1L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");
        final DiscussionReportRequest discussionReportRequest = new DiscussionReportRequest("토론 주제와 무관한 내용");

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(discussionReportRequest)
                .when().post("/api/v1/discussions/1/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("토론방을 수정한다")
    void updateDiscussionTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();

        final String updatedTitle = "상속과 조합은 어떤 상황에 쓰이나요?";
        final String updatedContent = "상속과 조합의 차이점이 궁금합니다.";
        final DiscussionUpdateRequest discussionUpdateRequest = new DiscussionUpdateRequest(
                updatedTitle,
                updatedContent
        );

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(discussionUpdateRequest)
                .when().patch("/api/v1/discussions/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("다른 사용자의 토론방 수정 시 에러가 발생한다")
    void updateDiscussion_unauthorized() {
        // given
        final String loginUserEmail = "user2@gmail.com";

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo(loginUserEmail, "user2", "https://image.png", "message");
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();

        final String token = MemberFixture.getTestAccessToken(loginUserEmail);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(new DiscussionUpdateRequest("title", "content"))
                .when().patch("/api/v1/discussions/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("토론방을 삭제한다")
    void deleteDiscussionTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().delete("/api/v1/discussions/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("댓글이 있는 토론방 삭제 시 에러가 발생한다")
    void deleteDiscussion_hasComments() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().delete("/api/v1/discussions/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("토론방 좋아요를 생성한다")
    void createDiscussionToggleLikeTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/discussions/1/like")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("토론방 좋아요를 삭제한다")
    void deleteDiscussionToggleLikeTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDiscussionLikeInfo(1L, 1L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/discussions/1/like")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("활성화된 토론방을 조회한다")
    void getActiveDiscussions() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 1L;

        databaseInitializer.setDiscussionInfo("게시글1", "내용1", memberId, bookId);
        databaseInitializer.setDiscussionInfo("게시글2", "내용2", memberId, bookId);
        databaseInitializer.setDiscussionInfo("게시글3", "내용3", memberId, bookId);
        databaseInitializer.setDiscussionInfo("게시글4", "내용4", memberId, bookId);

        final LocalDateTime base = LocalDateTime.now();

        databaseInitializer.setCommentInfo("4-1", memberId, 4L, base.minusMinutes(70)); // id = 1
        databaseInitializer.setCommentInfo("3-1", memberId, 3L, base.minusMinutes(60)); // id = 2
        databaseInitializer.setCommentInfo("3-2", memberId, 3L, base.minusMinutes(50));
        databaseInitializer.setCommentInfo("3-3", memberId, 3L, base.minusMinutes(40));
        databaseInitializer.setCommentInfo("2-1", memberId, 2L, base.minusMinutes(30)); // id = 3
        databaseInitializer.setCommentInfo("2-2", memberId, 2L, base.minusMinutes(20));
        databaseInitializer.setCommentInfo("1-1", memberId, 1L, base.minusMinutes(10)); // id = 4

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");
        final int size = 3;

        // when - then

        // 1페이지 확인
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .queryParam("period", 7)
                .queryParam("size", size)
                .when().get("/api/v1/discussions/active")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", equalTo(size))
                .body("pageInfo.hasNext", equalTo(true))
                .body("pageInfo.nextCursor", notNullValue())
                .body("items[0].discussionId", equalTo(1))
                .body("items[1].discussionId", equalTo(2))
                .body("items[2].discussionId", equalTo(3))
                .extract();

        // 2페이지 확인
        String cursor = response.jsonPath().getString("pageInfo.nextCursor");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .queryParam("period", 7)
                .queryParam("size", size)
                .queryParam("cursor", cursor)
                .when().get("/api/v1/discussions/active")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("items.size()", equalTo(1))
                .body("items[0].discussionId", equalTo(4))
                .body("pageInfo.hasNext", equalTo(false))
                .body("pageInfo.nextCursor", nullValue());
    }

    @Test
    @DisplayName("좋아요한 토론방을 전체 조회한다")
    void getLikedDiscussionsByMe() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        // 좋아요한 토론방 3개 생성
        databaseInitializer.setDiscussionInfo("좋아요 토론1", "내용1", 1L, 1L);
        databaseInitializer.setDiscussionLikeInfo(1L, 1L);

        databaseInitializer.setDiscussionInfo("좋아요 토론2", "내용2", 1L, 1L);
        databaseInitializer.setDiscussionLikeInfo(1L, 2L);

        databaseInitializer.setDiscussionInfo("좋아요 토론3", "내용3", 1L, 1L);
        databaseInitializer.setDiscussionLikeInfo(1L, 3L);

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when().get("/api/v1/discussions/liked")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Disabled
    @Nested
    @DisplayName("토론방 필터링 조회 테스트")
    class FilterDiscussionTest {

        @Test
        @DisplayName("토론방을 필터링한다")
        void filterDiscussions() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDiscussionInfo("오브젝트", "오브젝트 토론입니다", 1L, 1L);

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .when().get("/api/v1/discussions/search?keyword=오브젝트&type=ALL")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }
    }
}
