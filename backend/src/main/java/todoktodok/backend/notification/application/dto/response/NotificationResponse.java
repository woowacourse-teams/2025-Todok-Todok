package todoktodok.backend.notification.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.notification.domain.Notification;

public record NotificationResponse(
        int notReadCount,
        NotificationData data,
        boolean isRead,
        LocalDateTime createdAt
) {

    public NotificationResponse(
            final Long notReadCount,
            final Notification notification
    ) {
        this(
                notReadCount.intValue(),
                new NotificationData(notification),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
