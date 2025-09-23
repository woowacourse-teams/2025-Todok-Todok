package todoktodok.backend.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(max = 8, message = "닉네임은 1자 이상, 8자 이하여야 합니다")
        String nickname,

        @NotBlank(message = "idToken을 입력해주세요")
        String googleIdToken
) {
}
