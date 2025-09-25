package todoktodok.backend.notification.application.dto.response;

import todoktodok.backend.notification.domain.Notification;

public record NotificationData(
        Long notificationId,
        Long discussionId,
        Long commentId,
        Long replyId,
        String memberNickname,
        String discussionTitle,
        String content,
        String type,
        String target
) {
    public NotificationData(
            final Notification notification
    ) {
        this(
                notification.getId(),
                notification.getDiscussionId(),
                notification.getCommentId(),
                notification.getReplyId(),
                notification.getMemberNickname(),
                notification.getDiscussionTitle(),
                notification.getContent(),
                notification.getNotificationType().name(),
                notification.getNotificationTarget().name()
        );
    }
}
