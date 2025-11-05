package todoktodok.backend.member.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestLegacy(
        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(max = 8, message = "닉네임은 1자 이상, 8자 이하여야 합니다")
        String nickname,

        @NotBlank(message = "프로필 이미지를 입력해주세요")
        String profileImage,

        @Email(message = "올바른 이메일 형식을 입력해주세요")
        String email
) {
}
