package todoktodok.backend.member.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import io.jsonwebtoken.JwtException;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.service.query.DiscussionQueryService;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.global.jwt.TokenInfo;
import todoktodok.backend.member.application.dto.request.*;
import todoktodok.backend.member.application.dto.response.MemberResponse;
import todoktodok.backend.member.application.dto.response.ProfileUpdateResponse;
import todoktodok.backend.member.application.dto.response.TokenResponse;
import todoktodok.backend.member.domain.repository.RefreshTokenRepository;
import todoktodok.backend.member.infrastructure.GoogleAuthClient;

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

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private DiscussionQueryService discussionQueryService;

    @MockitoBean
    private GoogleAuthClient googleAuthClient;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }


    @Test
    @DisplayName("기존 회원 로그인 시 엑세스 토큰과 리프레시 토큰을 발급한다")
    void loginUserTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String email = "user@gmail.com";
        given(googleAuthClient.resolveVerifiedEmailFrom(anyString())).willReturn(email);

        final String fakeToken = "fakeToken_willNotUse";
        final LoginRequest fakeLoginRequest = new LoginRequest(fakeToken);

        // when
        final TokenResponse tokenResponse = memberCommandService.login(fakeLoginRequest);
        final TokenInfo accessTokenInfo = jwtTokenProvider.getInfoByAccessToken(tokenResponse.accessToken());
        final TokenInfo refreshTokenInfo = jwtTokenProvider.getInfoByRefreshToken(tokenResponse.refreshToken());

        // then
        assertAll(
                () -> assertThat(accessTokenInfo.role()).isEqualTo(Role.USER),
                () -> assertThat(refreshTokenInfo.role()).isEqualTo(Role.USER)
        );
    }

    @Test
    @DisplayName("신규 회원 로그인 시 임시 토큰을 발급한다")
    void loginTempUserTest() {
        // given
        final LoginRequestLegacy loginRequest = new LoginRequestLegacy("user@gmail.com");

        final String email = "user@gmail.com";
        given(googleAuthClient.resolveVerifiedEmailFrom(anyString())).willReturn(email);

        final String fakeToken = "fakeToken_willNotUse";
        final LoginRequest fakeLoginRequest = new LoginRequest(fakeToken);

        // when
        final TokenResponse tokenResponse = memberCommandService.login(fakeLoginRequest);
        final TokenInfo accessTokenInfo = jwtTokenProvider.getInfoByAccessToken(tokenResponse.accessToken());

        // then
        assertAll(
                () -> assertThat(accessTokenInfo.role()).isEqualTo(Role.TEMP_USER),
                () -> assertThat(tokenResponse.refreshToken()).isNull()
        );
    }

    @Test
    @DisplayName("임시 토큰으로 회원 가입 시 엑세스 토큰과 리프레시 토큰을 발급한다")
    void signUpTest() {
        // given
        final String email = "user@gmail.com";
        final SignupRequest signupRequest = new SignupRequest("user", "https://user.png", email);

        // when
        final TokenResponse tokenResponse = memberCommandService.signup(signupRequest, email);
        final TokenInfo accessTokenInfo = jwtTokenProvider.getInfoByAccessToken(tokenResponse.accessToken());
        final TokenInfo refreshTokenInfo = jwtTokenProvider.getInfoByRefreshToken(tokenResponse.refreshToken());

        // then
        assertAll(
                () -> assertThat(accessTokenInfo.role()).isEqualTo(Role.USER),
                () -> assertThat(refreshTokenInfo.role()).isEqualTo(Role.USER)
        );
    }

    @Test
    @DisplayName("엑세스 토큰과 리프레시 토큰을 재발급한다")
    void refreshTest() {
        // given
        final String email = "user@gmail.com";
        final String nickname = "nick";
        final String profileImage = "https://image.png";
        final String profileMessage = "profileMessage";
        databaseInitializer.setUserInfo(email, nickname, profileImage, profileMessage);

        given(googleAuthClient.resolveVerifiedEmailFrom(anyString())).willReturn(email);

        final String fakeToken = "fakeToken_willNotUse";
        final LoginRequest fakeLoginRequest = new LoginRequest(fakeToken);

        final TokenResponse oldTokenResponse = memberCommandService.login(fakeLoginRequest);
        final String oldRefreshToken = oldTokenResponse.refreshToken();
        final RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(oldRefreshToken);

        // when
        final TokenResponse tokenResponse = memberCommandService.refresh(refreshTokenRequest);
        final TokenInfo accessTokenInfo = jwtTokenProvider.getInfoByAccessToken(tokenResponse.accessToken());
        final TokenInfo refreshTokenInfo = jwtTokenProvider.getInfoByRefreshToken(tokenResponse.refreshToken());

        // then
        assertAll(
                () -> assertThat(accessTokenInfo.role()).isEqualTo(Role.USER),
                () -> assertThat(refreshTokenInfo.role()).isEqualTo(Role.USER),
                () -> assertThat(accessTokenInfo.id()).isEqualTo(1L),
                () -> assertThat(refreshTokenInfo.id()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("유효하지 않은 리프레시 토큰으로 재발급을 요청하면 예외가 발생한다")
    void refreshTest_notValidate_fail() {
        // given
        final String email = "user@gmail.com";
        final String nickname = "nick";
        final String profileImage = "https://image.png";
        final String profileMessage = "profileMessage";
        databaseInitializer.setUserInfo(email, nickname, profileImage, profileMessage);

        final RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("notValidateToken");

        // when - then
        assertThatThrownBy(() -> memberCommandService.refresh(refreshTokenRequest))
                .isInstanceOf(JwtException.class)
                .hasMessageContaining("잘못된 로그인 시도입니다. 다시 시도해 주세요");
    }

    @Test
    @DisplayName("회원가입 시 중복된 닉네임을 입력하면 예외가 발생한다")
    void validateDuplicatedNicknameTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String nickname = "user";
        final SignupRequest signupRequest = new SignupRequest(nickname, "https://user.png", "user22@gmail.com");

        // when - then
        assertThatThrownBy(() -> memberCommandService.signup(signupRequest, "user22@gmail.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 닉네임입니다");
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
                .hasMessageContaining("이미 가입된 이메일입니다");
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
                .hasMessageContaining("소셜 로그인을 하지 않은 이메일입니다");
    }

    @Test
    @DisplayName("자기 자신을 차단하면 예외가 발생한다")
    void validateSelfBlockTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final Long memberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberCommandService.block(memberId, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신을 차단할 수 없습니다");
    }

    @Test
    @DisplayName("자기 자신을 신고하면 예외가 발생한다")
    void validateSelfReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final Long memberId = 1L;
        final String reason = "욕설/인신공격";

        // when - then
        assertThatThrownBy(() -> memberCommandService.report(memberId, memberId, reason))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신을 신고할 수 없습니다");
    }

    @Test
    @DisplayName("이미 자신이 차단한 회원을 중복 차단하면 예외가 발생한다")
    void validateDuplicatedBlockTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "");

        final Long memberId = 1L;
        final Long targetId = 2L;

        memberCommandService.block(memberId, targetId);

        // when - then
        assertThatThrownBy(() -> memberCommandService.block(memberId, targetId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 차단한 회원입니다");
    }

    @Test
    @DisplayName("이미 자신이 신고한 회원을 중복 신고하면 예외가 발생한다")
    void validateDuplicatedReportTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "");

        final Long memberId = 1L;
        final Long targetId = 2L;
        final String reason = "욕설/인신공격";

        memberCommandService.report(memberId, targetId, reason);

        // when - then
        assertThatThrownBy(() -> memberCommandService.report(memberId, targetId, reason))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 신고한 회원입니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 정보를 수정하려고 하면 예외가 발생한다")
    void updateProfileTest_memberNotFound_fail() {
        // given
        final Long notExistsMemberId = 1L;
        final ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest("nickname", "profileMessage");

        // when - then
        assertThatThrownBy(() -> memberCommandService.updateProfile(notExistsMemberId, profileUpdateRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("정보수정 시 다른사람과 중복된 닉네임을 입력하면 예외가 발생한다")
    void updateProfileTest_validateDuplicateNickname_fail() {
        // given
        databaseInitializer.setUserInfo("user@gmail.com", "nick", "https://image.png", "profileMessage");
        databaseInitializer.setUserInfo("user2@gmail.com", "nickname", "https://image.png", "profileMessage");

        final Long memberId = 1L;
        final ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest("nickname", "newProfileMessage");

        // when - then
        assertThatThrownBy(() -> memberCommandService.updateProfile(memberId, profileUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 닉네임입니다");
    }

    @Test
    @DisplayName("정보수정 시 이전의 내 정보와 동일한 정보라면 예외가 발생하지 않는다")
    void updateProfileTest_isMyNickname_success() {
        // given
        final String nickname = "nickname";
        final String profileMessage = "profileMessage";
        databaseInitializer.setUserInfo("user@gmail.com", nickname, "https://image.png", profileMessage);

        final Long memberId = 1L;
        final ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(nickname, profileMessage);

        // when
        final ProfileUpdateResponse expected = new ProfileUpdateResponse(nickname, profileMessage);

        // then
        assertThat(memberCommandService.updateProfile(memberId, profileUpdateRequest)).isEqualTo(expected);
    }

    @Test
    @DisplayName("프로필 사진 수정 시 파일이 비어있다면 예외가 발생한다")
    void updateProfileImageTest_isEmpty_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        final Long memberId = 1L;
        final MultipartFile multipartFile = new MockMultipartFile(
                "profileImage",
                "empty.jpg",
                "image/jpg",
                new byte[]{}
        );

        // when - then
        assertThatThrownBy(() -> memberCommandService.updateProfileImage(memberId, multipartFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("파일이 비어있습니다");
    }

    @Test
    @DisplayName("프로필 사진 수정 시 파일 크기가 5MB 이상이면 예외가 발생한다")
    void updateProfileImageTest_isOverSize_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        final Long memberId = 1L;
        final MultipartFile multipartFile = new MockMultipartFile(
                "profileImage",
                "over5Mb.png",
                "image/png",
                new byte[6 * 1024 * 1024]
        );

        // when - then
        assertThatThrownBy(() -> memberCommandService.updateProfileImage(memberId, multipartFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("파일 크기가 5MB 초과입니다");
    }

    @Test
    @DisplayName("프로필 사진 수정 시 확장자가 이미지 파일이 아니라면 예외가 발생한다")
    void updateProfileImageTest_isNotImageType_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        final Long memberId = 1L;
        final MultipartFile multipartFile = new MockMultipartFile(
                "profileImage",
                "notImageType.txt",
                "text/plain",
                "hello".getBytes(StandardCharsets.UTF_8)
        );

        // when - then
        assertThatThrownBy(() -> memberCommandService.updateProfileImage(memberId, multipartFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미지 파일이 아닙니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원이 차단해제를 하면 예외가 발생한다")
    void deleteBlockTest_notFoundMember_fail() {
        // given
        databaseInitializer.setUserInfo("target@gmail.com", "target", "https://image.png", "targe");
        final Long notExistsMemberId = 999L;
        final Long targetId = 1L;

        // when - then
        assertThatThrownBy(() -> memberCommandService.deleteBlock(notExistsMemberId, targetId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원을 차단해제하면 예외가 발생한다")
    void deleteBlockTest_notFoundTarget_fail() {
        // given
        databaseInitializer.setUserInfo("user@gmail.com", "user", "https://image.png", "user");
        final Long memberId = 1L;
        final Long notExistsTargetId = 999L;

        // when - then
        assertThatThrownBy(() -> memberCommandService.deleteBlock(memberId, notExistsTargetId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("차단하지 않은 회원을 차단해제하면 예외가 발생한다")
    void deleteBlockTest_notBlock_fail() {
        // given
        databaseInitializer.setUserInfo("user@gmail.com", "user", "https://image.png", "user");
        databaseInitializer.setUserInfo("target@gmail.com", "target", "https://image.png", "targe");

        final Long memberId = 1L;
        final Long targetId = 2L;

        // when - then
        assertThatThrownBy(() -> memberCommandService.deleteBlock(memberId, targetId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차단한 회원이 아닙니다");
    }

    @Nested
    @DisplayName("탈퇴한 회원 테스트")
    class DeletedMemberTest {

        @Test
        @DisplayName("회원을 탈퇴하면 회원과 리프레시 토큰이 삭제되고 회원 서비스를 사용할 수 없다")
        void deleteMemberTest() {
            // given
            final String email = "user@gmail.com";

            databaseInitializer.setUserInfo(email, "user", "https://user.png", "");
            databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user.png", "");

            given(googleAuthClient.resolveVerifiedEmailFrom(anyString())).willReturn(email);

            final String fakeToken = "fakeToken_willNotUse";
            final LoginRequest fakeLoginRequest = new LoginRequest(fakeToken);

            final TokenResponse tokenResponse = memberCommandService.login(fakeLoginRequest);
            final Long memberId = 1L;
            final String refreshToken = tokenResponse.refreshToken();

            // when
            memberCommandService.deleteMember(memberId, new RefreshTokenRequest(tokenResponse.refreshToken()));

            // then
            assertAll(
                    () -> assertThatThrownBy(() -> memberCommandService.deleteBlock(memberId, 2L))
                            .isInstanceOf(NoSuchElementException.class)
                            .hasMessageContaining("해당 회원을 찾을 수 없습니다"),
                    () -> assertThatThrownBy(() -> memberCommandService.refresh(new RefreshTokenRequest(refreshToken)))
                            .isInstanceOf(NoSuchElementException.class)
                            .hasMessageContaining("해당 회원을 찾을 수 없습니다")
            );
        }

        @Test
        @DisplayName("탈퇴한 회원 로그인 시 재가입 후 액세스 토큰과 리프레시 토큰을 발급한다")
        void loginDeletedUserTest() {
            // given
            final String email = "user@gmail.com";

            databaseInitializer.setUserInfo(email, "user", "", "");
            databaseInitializer.deleteUserInfo(email);

            given(googleAuthClient.resolveVerifiedEmailFrom(anyString())).willReturn(email);

            final String fakeToken = "fakeToken_willNotUse";
            final LoginRequest fakeLoginRequest = new LoginRequest(fakeToken);

            // when
            final TokenResponse tokenResponse = memberCommandService.login(fakeLoginRequest);
            final TokenInfo accessTokenInfo = jwtTokenProvider.getInfoByAccessToken(tokenResponse.accessToken());
            final TokenInfo refreshTokenInfo = jwtTokenProvider.getInfoByRefreshToken(tokenResponse.refreshToken());

            // then
            assertAll(
                    () -> assertThat(accessTokenInfo.role()).isEqualTo(Role.USER),
                    () -> assertThat(refreshTokenInfo.role()).isEqualTo(Role.USER)
            );
        }

        @Test
        @DisplayName("회원 탈퇴 시 리프레시 토큰과 액세스 토큰의 memberId가 불일치하면 예외가 발생한다")
        void deleteMemberTest_notFoundMember_fail() {
            // given
            final String email = "user@gmail.com";

            databaseInitializer.setUserInfo(email, "user", "https://user.png", "");

            given(googleAuthClient.resolveVerifiedEmailFrom(anyString())).willReturn(email);

            final String fakeToken = "fakeToken_willNotUse";
            final LoginRequest fakeLoginRequest = new LoginRequest(fakeToken);

            final TokenResponse tokenResponse = memberCommandService.login(fakeLoginRequest);
            final Long wrongMemberId = 999L;

            // when - then
            assertThatThrownBy(() -> memberCommandService.deleteMember(wrongMemberId, new RefreshTokenRequest(tokenResponse.refreshToken())))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("리프레시 토큰과 액세스 토큰의 회원 정보가 일치하지 않습니다");
        }

        @Test
        @DisplayName("특정 토론방을 조회할 때 탈퇴한 회원의 토론방도 조회한다")
        void getDiscussion_includeDeletedMember() {
            // given
            final Long deletedUserId = 1L;
            final String deletedUserEmail = "user1@gmail.com";

            databaseInitializer.setDefaultBookInfo();
            databaseInitializer.setUserInfo(deletedUserEmail, "user1", "", "");
            databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");
            databaseInitializer.setDiscussionInfo("user1의 토론방", "토론방 내용입니다", deletedUserId, 1L);

            databaseInitializer.deleteUserInfo(deletedUserEmail);

            final Long discussionId = 1L;
            final Long existingMemberId = 2L;

            // when
            final DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(existingMemberId, discussionId);
            final MemberResponse memberResponse = discussionResponse.member();

            // then
            assertAll(
                    () -> assertThat(discussionResponse.discussionId()).isEqualTo(discussionId),
                    () -> assertThat(memberResponse.memberId()).isEqualTo(deletedUserId),
                    () -> assertThat(memberResponse.nickname()).isEqualTo("(알수없음)"),
                    () -> assertThat(memberResponse.profileImage()).isEqualTo("https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/todoktodok-images/profile/todoki.png")
            );
        }
    }
}
