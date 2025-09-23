package todoktodok.backend.notification.presentation;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.application.service.command.NotificationCommandService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationCommandService notificationCommandService;

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@LoginMember final Long memberId) {
        final List<NotificationResponse> notifications = notificationCommandService.getNotifications(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(notifications);
    }
}
