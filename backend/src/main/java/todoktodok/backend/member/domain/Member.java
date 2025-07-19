package todoktodok.backend.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import todoktodok.backend.global.common.TimeStamp;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
public class Member extends TimeStamp {

    private static final int NICKNAME_MAX_LENGTH = 8;
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
            String email, String nickname, String profileImage, String profileMessage
    ) {
        validateNickname(nickname);
        validateEmail(email);

        return new Member(
                null, email, nickname, profileImage, profileMessage
        );
    }

    private static void validateNickname(final String nickname) {
        validateNicknameLength(nickname);
        validateForbiddenNickname(nickname);
        validateNicknamePattern(nickname);
    }

    private static void validateForbiddenNickname(final String nickname) {
        if (nickname.equals(DELETED_MEMBER_NICKNAME)) {
            throw new IllegalArgumentException("다른 닉네임을 사용해주세요");
        }
    }

    private static void validateNicknamePattern(final String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new IllegalArgumentException("닉네임은 한글, 영어, 숫자만 입력해주세요 (특수문자, 공백 금지)");
        }
    }

    private static void validateNicknameLength(final String nickname) {
        if (nickname.isEmpty() || nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("닉네임은 1자 이상, 8자 이하여야 합니다");
        }
    }

    private static void validateEmail(final String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다");
        }
    }
}
