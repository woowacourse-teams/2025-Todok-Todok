package todoktodok.backend.notification.presentation;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.application.dto.response.UnreadNotificationResponse;
import todoktodok.backend.notification.application.service.command.NotificationCommandService;
import todoktodok.backend.notification.application.service.query.NotificationQueryService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController implements NotificationApiDocs{

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @Auth(value = Role.USER)
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@LoginMember final Long memberId) {
        final List<NotificationResponse> notifications = notificationQueryService.getNotifications(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(notifications);
    }

    @Auth(value = Role.USER)
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @LoginMember final Long memberId,
            @PathVariable final Long notificationId
    ) {
        notificationCommandService.markNotificationAsRead(memberId, notificationId);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Auth(value = Role.USER)
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @LoginMember final Long memberId,
            @PathVariable final Long notificationId
    ) {
        notificationCommandService.deleteNotification(memberId, notificationId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Auth(value = Role.USER)
    @GetMapping("/unread/exists")
    public ResponseEntity<UnreadNotificationResponse> hasUnreadNotifications(
            @LoginMember final Long memberId
    ) {
        UnreadNotificationResponse unreadNotificationResponse = notificationQueryService.checkUnReadNotification(
                memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(unreadNotificationResponse);
    }
}
