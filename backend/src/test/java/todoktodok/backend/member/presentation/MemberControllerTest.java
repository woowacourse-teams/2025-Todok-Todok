package todoktodok.backend.member.presentation;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
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
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.ProfileUpdateRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
class MemberControllerTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("로그인한다")
    void loginTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String email = "user@gmail.com";

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email))
                .when().post("/api/v1/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("회원가입을 한다")
    void signUpTest() {
        // given
        final String email = "email@gmail.com";
        final String nickname = "test";
        final String profileImage = "https://www.image.com";
        final String tempToken = jwtTokenProvider.createTempToken(email);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", tempToken)
                .body(new SignupRequest(nickname, profileImage, email))
                .when().post("/api/v1/members/signup")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("회원을 차단한다")
    void blockTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        final String token = MemberFixture.login("user@gmail.com");

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
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user");

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/members/2/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    @DisplayName("프로필을 조회한다")
    void getProfileTest(final Long memberId) {
        // given
        databaseInitializer.setUserInfo("user@gmail.com", "user", "https://user.png", "user");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user2");

        final String token = MemberFixture.login("user@gmail.com");

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
        databaseInitializer.setUserInfo("user@gmail.com", "user", "https://user.png", "user");

        final String token = MemberFixture.login("user@gmail.com");
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
        databaseInitializer.setUserInfo("user@gmail.com", "user", "https://user.png", "user");

        final String token = MemberFixture.login("user@gmail.com");
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
}
