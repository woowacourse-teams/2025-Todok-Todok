package todoktodok.backend.notification.application.service.command;

import todoktodok.backend.notification.domain.NotificationTarget;
import todoktodok.backend.notification.domain.NotificationType;

public record CreateNotificationRequest(
        Long recipientId,
        Long discussionId,
        Long commentId,
        Long replyId,
        String memberNickname,
        String discussionTitle,
        String content,
        NotificationType type,
        NotificationTarget target
) {
}
