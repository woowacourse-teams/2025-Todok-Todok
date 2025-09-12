package todoktodok.backend.notification.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NotificationTokenRequest(
        @NotBlank(message = "FCM registration token을 입력해주세요")
        String token,
        @NotBlank(message = "Firebase Installation Id를 입력해주세요")
        String fid
) {
}
