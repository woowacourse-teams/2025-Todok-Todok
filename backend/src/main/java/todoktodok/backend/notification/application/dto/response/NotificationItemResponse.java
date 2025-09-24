package todoktodok.backend.notification.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.notification.domain.Notification;

public record NotificationItemResponse(
        NotificationData data,
        boolean isRead,
        LocalDateTime createdAt
) {

    public NotificationItemResponse(Notification notification) {
        this(
                new NotificationData(notification),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
