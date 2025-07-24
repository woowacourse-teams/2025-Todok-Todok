package todoktodok.backend.member.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.global.jwt.TokenInfo;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.application.dto.request.SignupRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class MemberCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private MemberCommandService memberCommandService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("기존 회원 로그인 시 회원 토큰을 발급한다")
    void loginUserTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final LoginRequest loginRequest = new LoginRequest("user@gmail.com");

        // when
        final String token = memberCommandService.login(loginRequest);
        final TokenInfo tokenInfo = jwtTokenProvider.getInfo(token);

        // then
        assertThat(tokenInfo.role()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("신규 회원 로그인 시 임시 토큰을 발급한다")
    void loginTempUserTest() {
        // given
        final LoginRequest loginRequest = new LoginRequest("user@gmail.com");

        // when
        final String token = memberCommandService.login(loginRequest);
        final TokenInfo tokenInfo = jwtTokenProvider.getInfo(token);

        // then
        assertThat(tokenInfo.role()).isEqualTo(Role.TEMP_USER);
    }

    @Test
    @DisplayName("임시 토큰으로 회원 가입 시 회원 토큰을 발급한다")
    void signUpTest() {
        // given
        final String email = "user@gmail.com";
        final SignupRequest signupRequest = new SignupRequest("user", "https://user.png", email);

        // when
        final String token = memberCommandService.signup(signupRequest, email);
        final TokenInfo tokenInfo = jwtTokenProvider.getInfo(token);

        // then
        assertThat(tokenInfo.role()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("회원가입 시 중복된 닉네임을 입력하면 예외가 발생한다")
    void validateDuplicatedNicknameTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String nickname = "user";
        final SignupRequest signupRequest = new SignupRequest(nickname, "https://user.png",  "user22@gmail.com");

        // when - then
        assertThatThrownBy(() -> memberCommandService.signup(signupRequest,  "user22@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 닉네임입니다");
    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일을 입력하면 예외가 발생한다")
    void validateDuplicatedEmailTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String email = "user@gmail.com";
        final SignupRequest signupRequest = new SignupRequest("user22", "https://user.png", email);

        // when - then
        assertThatThrownBy(() -> memberCommandService.signup(signupRequest, email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 가입된 이메일입니다");
    }

    @Test
    @DisplayName("소셜 로그인을 하지 않은 유저가 회원가입을 시도할 경우 예외가 발생한다")
    void validateEmailWithTokenEmailTest() {
        // given
        final String email = "user@gmail.com";
        final SignupRequest signupRequest = new SignupRequest("user", "https://user.png", email);

        // when - then
        assertThatThrownBy(() -> memberCommandService.signup(signupRequest, "notLoginUser@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소셜 로그인을 하지 않은 이메일입니다");
    }

    @Test
    @DisplayName("자기 자신을 차단하면 예외가 발생한다")
    void validateSelfBlockTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        Long memberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberCommandService.block(memberId, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신을 차단할 수 없습니다");
    }

    @Test
    @DisplayName("자기 자신을 신고하면 예외가 발생한다")
    void validateSelfReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        Long memberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberCommandService.report(memberId, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신을 신고할 수 없습니다");
    }

    @Test
    @DisplayName("이미 자신이 차단한 회원을 중복 차단하면 예외가 발생한다")
    void validateDuplicatedBlockTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "");
        Long memberId = 1L;
        Long targetId = 2L;

        memberCommandService.block(memberId, targetId);

        // when - then
        assertThatThrownBy(() -> memberCommandService.block(memberId, targetId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 차단한 회원입니다");
    }

    @Test
    @DisplayName("이미 자신이 신고한 회원을 중복 신고하면 예외가 발생한다")
    void validateDuplicatedReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "");
        Long memberId = 1L;
        Long targetId = 2L;

        memberCommandService.report(memberId, targetId);

        // when - then
        assertThatThrownBy(() -> memberCommandService.report(memberId, targetId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신고한 회원입니다");
    }
}
