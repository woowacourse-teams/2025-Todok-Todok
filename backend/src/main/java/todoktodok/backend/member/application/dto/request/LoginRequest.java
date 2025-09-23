package todoktodok.backend.member.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "idToken을 입력해주세요")
        String googleIdToken
) {
}
