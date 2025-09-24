package todoktodok.backend.notification.application.dto.response;

import java.util.List;

public record NotificationResponse(
        int unreadCount,
        List<NotificationItemResponse> notifications
) {

    public NotificationResponse(
            final Long unreadCount,
            final List<NotificationItemResponse> notificationItemResponses
    ) {
        this(
                unreadCount.intValue(),
                notificationItemResponses
        );
    }
}
