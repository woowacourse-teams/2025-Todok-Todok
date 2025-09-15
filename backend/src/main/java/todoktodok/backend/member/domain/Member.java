package todoktodok.backend.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import todoktodok.backend.global.common.TimeStamp;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
public class Member extends TimeStamp {

    private static final int NICKNAME_MAX_LENGTH = 8;
    private static final int PROFILE_MESSAGE_MAX_LENGTH = 40;
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]+$");
    private static final String DELETED_MEMBER_NICKNAME = "(알수없음)";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    private String profileMessage;

    @Builder
    public static Member create(
            final String email,
            final String nickname,
            final String profileImage,
            final String profileMessage
    ) {
        validateNickname(nickname);
        validateEmail(email, nickname);

        return new Member(
                null, email, nickname, profileImage, profileMessage
        );
    }

    public void updateNicknameAndProfileMessage(
            final String nickname,
            final String profileMessage
    ) {
        validateNickname(nickname);
        validateProfileMessage(profileMessage);

        this.nickname = nickname;
        this.profileMessage = profileMessage;
    }

    public void updateProfileImage(final String profileImage) {
        this.profileImage = profileImage;
    }

    public void resignUp() {
        if (isDeleted()){
            cancelDeletion();
            return;
        }
        throw new IllegalArgumentException(String.format("탈퇴한 회원이 아닙니다: memberId = %s", this.id));
    }

    public boolean isMyNickname(final String nickname) {
        return this.nickname.equals(nickname);
    }

    public void validateSelfBlock(final Member target) {
        if (this.equals(target)) {
            throw new IllegalArgumentException(String.format("자기 자신을 차단할 수 없습니다: memberId = %s", this.id));
        }
    }

    public void validateSelfReport(final Member target) {
        if (this.equals(target)) {
            throw new IllegalArgumentException(String.format("자기 자신을 신고할 수 없습니다: memberId = %s", this.id));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> thisClass = org.hibernate.Hibernate.getClass(this);
        Class<?> thatClass = org.hibernate.Hibernate.getClass(o);
        if (thisClass != thatClass) return false;

        Member that = (Member) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    private static void validateNickname(final String nickname) {
        validateNicknameLength(nickname);
        validateForbiddenNickname(nickname);
        validateNicknamePattern(nickname);
    }

    private static void validateForbiddenNickname(final String nickname) {
        if (nickname.equals(DELETED_MEMBER_NICKNAME)) {
            throw new IllegalArgumentException(String.format("다른 닉네임을 사용해주세요: nickname = %s", nickname));
        }
    }

    private static void validateNicknamePattern(final String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException(String.format("닉네임은 한글, 영어, 숫자만 입력해주세요 (특수문자, 공백 금지): nickname = %s", nickname));
        }
    }

    private static void validateNicknameLength(final String nickname) {
        if (nickname == null || nickname.isEmpty() || nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("닉네임은 1자 이상, 8자 이하여야 합니다: %s자",
                    (nickname == null ? null : nickname.length())));
        }
    }

    private static void validateEmail(
            final String email,
            final String nickname
    ) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(String.format("이메일은 필수입니다: nickname = %s", nickname));
        }
    }

    private static void validateProfileMessage(final String profileMessage) {
        if (profileMessage == null) {
            return;
        }

        if (profileMessage.length() > PROFILE_MESSAGE_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("상태메세지는 40자 이하여야 합니다: %s자", profileMessage.length()));
        }
    }
}
