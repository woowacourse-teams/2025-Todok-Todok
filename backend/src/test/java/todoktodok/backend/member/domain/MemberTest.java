package todoktodok.backend.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

class MemberTest {

    @Test
    @DisplayName("닉네임에 '(알수없음)'을 사용할 수 없다")
    void validateForbiddenNicknameTest() {
        // given
        final String nickname = "(알수없음)";
        final String email = "email@gmail.com";
        final String profileImage = "https://www.image.com";

        // when - then
        assertThatThrownBy(() -> {
            Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .profileImage(profileImage)
                    .build();
        } ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 닉네임을 사용해주세요");
    }

    @ParameterizedTest
    @ValueSource(strings = {"test!", "te st"})
    @DisplayName("닉네임은 한글, 영어, 숫자만 입력할 수 있고, 특수문자, 공백은 입력할 수 없다")
    void validateNicknamePatternTest(String nickname) {
        // given
        final String email = "email@gmail.com";
        final String profileImage = "https://www.image.com";

        // when - then
        assertThatThrownBy(() -> {
            Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .profileImage(profileImage)
                    .build();
        } ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("닉네임은 한글, 영어, 숫자만 입력해주세요 (특수문자, 공백 금지)");
    }

    @Test
    @DisplayName("닉네임은 1자 이상, 8자 이하여야 한다")
    void validateNicknameLengthTest() {
        // given
        final String nickname = "123456789";
        final String email = "email@gmail.com";
        final String profileImage = "https://www.image.com";

        // when - then
        assertThatThrownBy(() -> {
            Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .profileImage(profileImage)
                    .build();
        } ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("닉네임은 1자 이상, 8자 이하여야 합니다");
    }

    @Test
    @DisplayName("이메일은 공백일 수 없다")
    void validateEmailTest() {
        // given
        final String nickname = "12345678";
        final String email = "";
        final String profileImage = "https://www.image.com";

        // when - then
        assertThatThrownBy(() -> {
            Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .profileImage(profileImage)
                    .build();
        } ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일은 필수입니다");
    }

    @Test
    @DisplayName("상태메세지는 40자 이하여야 한다")
    void validateProfileMessageTest() {
        // given
        final Member member = MemberFixture.create(
                "email@gmail.com",
                "nickname",
                "profileImage"
        );

        final String newNickname = "nicknick";
        final String newProfileMessage = "a".repeat(41);

        // when - then
        assertThatThrownBy(() -> {
            member.updateNicknameAndProfileMessage(newNickname, newProfileMessage);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상태메세지는 40자 이하여야 합니다");
    }

    @Test
    @DisplayName("내 닉네임과 동일하면 true를 반환한다")
    void isMyNicknameTest() {
        // given
        final Member member = MemberFixture.create(
                "email@gmail.com",
                "nickname",
                "profileImage"
        );

        final String equalNickname = "nickname";

        // when - then
        assertThat(member.isMyNickname(equalNickname)).isTrue();
    }
}
