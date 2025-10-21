package todoktodok.backend.member.presentation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.member.application.dto.request.MemberReportRequest;
import todoktodok.backend.member.application.dto.request.ProfileUpdateRequest;
import todoktodok.backend.member.application.dto.request.RefreshTokenRequest;
import todoktodok.backend.member.application.dto.response.TokenResponse;
import todoktodok.backend.member.infrastructure.AuthClient;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@Disabled
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
class MemberControllerTest {

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
    @DisplayName("토큰을 재발급한다")
    void refreshTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        final TokenResponse tokens = memberFixture.getAccessAndRefreshToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new RefreshTokenRequest(tokens.refreshToken()))
                .when().post("/api/v1/members/refresh")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("회원을 차단한다")
    void blockTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/members/2/block")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("회원을 신고한다")
    void reportTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);
        final MemberReportRequest memberReportRequest = new MemberReportRequest("욕설/인신공격");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(memberReportRequest)
                .when().post("/api/v1/members/2/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    @DisplayName("프로필을 조회한다")
    void getProfileTest(final Long memberId) {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setUserInfo(DEFAULT_EMAIL, "user", "https://user.png", "user");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user2");

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when
        final String uri = String.format("/api/v1/members/%d/profile", memberId);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get(uri)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("닉네임을 수정한다")
    void updateProfileTest_nickname() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setUserInfo(DEFAULT_EMAIL, "user", "https://user.png", "user");

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);
        final String newNickname = "newUser";
        final String profileMessage = "user";

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(new ProfileUpdateRequest(newNickname, profileMessage))
                .when().put("/api/v1/members/profile")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("nickname", equalTo(newNickname));
    }

    @Test
    @DisplayName("상태메세지를 수정한다")
    void updateProfileTest_profileMessage() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setUserInfo(DEFAULT_EMAIL, "user", "https://user.png", "user");

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);
        final String nickname = "user";
        final String newProfileMessage = "newProfileMessage";

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(new ProfileUpdateRequest(nickname, newProfileMessage))
                .when().put("/api/v1/members/profile")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("profileMessage", equalTo(newProfileMessage));
    }

    @Test
    @DisplayName("활동도서를 조회한다")
    void getActiveBooksTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user2");

        // 토론 생성한 책
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        // 댓글 작성한 책
        databaseInitializer.setBookInfo("토비의 스프링", "스프링 설명", "토비", "출판사", "1234567890123", "spring.png");
        databaseInitializer.setDiscussionInfo("스프링 토론ㄱ", "스프링 짱", 2L, 2L);
        databaseInitializer.setCommentInfo("맞지맞지 스프링 짱", 1L, 2L);

        // 대댓글 작성한 책
        databaseInitializer.setBookInfo("자바의정석", "자바 설명", "남궁성", "출판사", "1234567890123", "java.png");
        databaseInitializer.setDiscussionInfo("자바 토론ㄱ", "자바 짱", 2L, 3L);
        databaseInitializer.setCommentInfo("user2가 user2에 단 댓글", 2L, 3L);
        databaseInitializer.setReplyInfo("저도 자바좋아해요", 1L, 2L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/members/1/books")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Test
    @DisplayName("회원이 생성한 토론방을 조회한다")
    void getMemberDiscussionsByTypeTest_created() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/members/1/discussions?type=CREATED")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    @DisplayName("회원이 참여한 토론방을 조회한다")
    void getMemberDiscussionsByTypeTest_participated() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user2");

        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setBookInfo("토비의 스프링", "스프링 설명", "토비", "출판사", "1234567890123", "spring.png");
        databaseInitializer.setBookInfo("자바의정석", "자바 설명", "남궁성", "출판사", "1234567890123", "java.png");

        // 생성한 토론방
        databaseInitializer.setDefaultDiscussionInfo();

        // 댓글을 작성한 토론방
        databaseInitializer.setDiscussionInfo("스프링 토론ㄱ", "스프링 짱", 2L, 2L);
        databaseInitializer.setCommentInfo("맞지맞지 스프링 짱", 1L, 2L);

        // 대댓글 작성한 토론방
        databaseInitializer.setDiscussionInfo("자바 토론ㄱ", "자바 짱", 2L, 3L);
        databaseInitializer.setCommentInfo("user2가 user2에 단 댓글", 2L, 3L);
        databaseInitializer.setReplyInfo("저도 자바좋아해요", 1L, 2L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/members/1/discussions?type=PARTICIPATED")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Test
    @DisplayName("회원의 토론방을 필터링할 때 type을 명시하지 않으면 예외가 발생한다")
    void getMemberDiscussionsByTypeTest_noType_fail() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);
        final String uri = "/api/v1/members/1/discussions";

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get(uri)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("회원의 토론방을 필터링할 때 type에 정해지지 않는 값을 추가하면 예외가 발생한다")
    void getMemberDiscussionsByTypeTest_invalidType_fail() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);
        final String uri = "/api/v1/members/1/discussions?type=HELLO";

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get(uri)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("회원이 탈퇴한다")
    void deleteMemberTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setUserInfo(DEFAULT_EMAIL, "user", "https://image.png", "user");

        final TokenResponse tokenResponse = memberFixture.getAccessAndRefreshToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", tokenResponse.accessToken())
                .body(new RefreshTokenRequest(tokenResponse.refreshToken()))
                .when().delete("/api/v1/members")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("차단한 회원 전체를 조회한다")
    void getBlockMembersTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setUserInfo(DEFAULT_EMAIL, "user", "https://image.png", "user");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://image2.png", "user2");
        databaseInitializer.setUserInfo("user3@gmail.com", "user3", "https://image3.png", "user3");

        databaseInitializer.setBlockInfo(1L, 2L);
        databaseInitializer.setBlockInfo(1L, 3L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/members/block")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @Test
    @DisplayName("차단한 회원을 차단해제한다")
    void deleteBlockTest() {
        // given
        given(authClient.resolveVerifiedEmailFrom(anyString())).willReturn(DEFAULT_EMAIL);

        databaseInitializer.setUserInfo(DEFAULT_EMAIL, "user", "https://image.png", "user");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://image2.png", "user2");

        databaseInitializer.setBlockInfo(1L, 2L);

        final String token = memberFixture.getAccessToken(DEFAULT_EMAIL);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().delete("/api/v1/members/2/block")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
