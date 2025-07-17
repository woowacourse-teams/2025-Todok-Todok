package todoktodok.backend.member.application.dto.request;

import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email
        String email
) {
}
