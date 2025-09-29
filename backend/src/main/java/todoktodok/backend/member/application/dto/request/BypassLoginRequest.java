package todoktodok.backend.member.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BypassLoginRequest(
        @Email(message = "올바른 이메일 형식을 입력해주세요")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요")
        String password
) {
}
