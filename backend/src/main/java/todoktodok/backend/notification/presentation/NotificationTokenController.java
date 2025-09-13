package todoktodok.backend.notification.presentation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
import todoktodok.backend.global.resolver.LoginMember;
import todoktodok.backend.notification.application.dto.request.NotificationTokenRequest;
import todoktodok.backend.notification.application.service.command.NotificationTokenCommandService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/notificationTokens")
public class NotificationTokenController implements NotificationTokenApiDocs {

    private final NotificationTokenCommandService notificationTokenCommandService;

    @Auth(value = Role.USER)
    @PostMapping
    public ResponseEntity<Void> createNotificationToken(
            @LoginMember final Long memberId,
            @RequestBody @Valid final NotificationTokenRequest notificationTokenRequest
    ) {
        notificationTokenCommandService.save(memberId, notificationTokenRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
