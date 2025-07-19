package todoktodok.backend.member.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
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
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;
import todoktodok.backend.member.application.service.command.MemberCommandService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
class MemberControllerTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberCommandService memberCommandService;

    @Autowired
    private EntityManager em;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("비회원이 로그인 시도하면 임시 토큰을 발급한다")
    void loginTest() {
        // given
        final String email = "email@gmail.com";

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email))
                .when().post("/api/v1/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpTest() {
        // given
        final String email = "email@gmail.com";
        final String nickname = "test";
        final String profileImage = "https://www.image.com";
        final String tempToken = jwtTokenProvider.createTempToken();

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
        databaseInitializer.setUserInfo();
        memberCommandService.signup(new SignupRequest("user2", "https://user2.png", "user2@gmail.com")); //todo 이렇게 하는게 최선?

        String token = memberCommandService.login(new LoginRequest("user@gmail.com"));

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
        databaseInitializer.setUserInfo();
        memberCommandService.signup(new SignupRequest("user2", "https://user2.png", "user2@gmail.com")); //todo 이렇게 하는게 최선?

        String token = memberCommandService.login(new LoginRequest("user@gmail.com"));

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().post("/api/v1/members/2/report")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
