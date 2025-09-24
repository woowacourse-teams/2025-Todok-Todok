package todoktodok.backend.member.application.dto.request;

import jakarta.validation.constraints.Email;

public record BypassLoginRequest(
        @Email(message = "올바른 이메일 형식을 입력해주세요")
        String email
) {
}
