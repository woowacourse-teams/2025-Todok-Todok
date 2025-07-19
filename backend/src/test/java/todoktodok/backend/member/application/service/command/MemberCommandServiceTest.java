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
    @DisplayName("기존 회원 로그인 성공 테스트")
    void loginUserTest() {
        // given
        databaseInitializer.setUserInfo();
        final LoginRequest loginRequest = new LoginRequest("user@gmail.com");

        // when
        final String token = memberCommandService.login(loginRequest);
        final TokenInfo tokenInfo = jwtTokenProvider.getInfo(token);

        // then
        assertThat(tokenInfo.role()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("신규 회원 임시 토큰 발급 테스트")
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
    @DisplayName("회원가입 성공 테스트")
    void signUpTest() {
        // given
        final SignupRequest signupRequest = new SignupRequest("user", "https://user.png", "user@gmail.com");

        // when
        final String token = memberCommandService.signup(signupRequest);
        final TokenInfo tokenInfo = jwtTokenProvider.getInfo(token);

        // then
        assertThat(tokenInfo.role()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("닉네임 중복 테스트")
    void validateDuplicatedNicknameTest() {
        // given
        databaseInitializer.setUserInfo();
        final SignupRequest signupRequest = new SignupRequest("user", "https://user.png", "user@gmail.com");

        // when - then
        assertThatThrownBy(() -> memberCommandService.signup(signupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 닉네임입니다");
    }
}
