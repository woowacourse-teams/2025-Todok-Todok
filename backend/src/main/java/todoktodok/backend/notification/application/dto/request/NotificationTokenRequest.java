package todoktodok.backend.notification.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NotificationTokenRequest(
        @NotBlank(message = "토큰을 입력해주세요")
        String token
) {
}
