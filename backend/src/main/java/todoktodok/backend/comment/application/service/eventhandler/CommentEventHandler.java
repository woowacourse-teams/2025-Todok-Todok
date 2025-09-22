package todoktodok.backend.comment.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.comment.application.service.command.CommentCreated;
import todoktodok.backend.comment.application.service.command.CommentLikeCreated;
import todoktodok.backend.notification.application.service.command.CreateNotificationRequest;
import todoktodok.backend.notification.application.service.command.NotificationCommandService;
import todoktodok.backend.notification.domain.NotificationTarget;
import todoktodok.backend.notification.domain.NotificationType;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Component
@AllArgsConstructor
public class CommentEventHandler {

    public static final String HEART = "❤\uFE0F";
    public static final String TODOKTODOK_TITLE = "토독토독";

    private final FcmPushNotifier fcmPushNotifier;
    private final NotificationCommandService notificationCommandService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final CommentCreated commentCreated) {
        final Long recipientId = commentCreated.discussionMemberId();
        final Long authorId = commentCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("[%s님의 댓글 도착] %s",
                commentCreated.authorNickname(),
                commentCreated.discussionTitle()
        );
        final Long replyId = null;
        NotificationType notificationType = NotificationType.COMMENT;
        NotificationTarget notificationTarget = NotificationTarget.COMMENT;

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                TODOKTODOK_TITLE,
                notificationBody,
                commentCreated.discussionId(),
                commentCreated.commentId(),
                replyId,
                commentCreated.authorNickname(),
                commentCreated.discussionTitle(),
                commentCreated.content(),
                notificationType.name(),
                notificationTarget.name()
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);

        final CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest(
                commentCreated.discussionMemberId(),
                commentCreated.discussionId(),
                commentCreated.commentId(),
                replyId,
                commentCreated.authorNickname(),
                commentCreated.discussionTitle(),
                commentCreated.content(),
                notificationType,
                notificationTarget
        );
        notificationCommandService.createNotification(createNotificationRequest);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final CommentLikeCreated commentLikeCreated) {
        final Long recipientId = commentLikeCreated.commentMemberId();
        final Long authorId = commentLikeCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("%s 님이 [%s] 토론방의 댓글에 %s를 보냈습니다",
                commentLikeCreated.commentMemberId(),
                commentLikeCreated, commentLikeCreated.discussionTitle(),
                HEART
        );
        final Long replyId = null;
        final String content = "";
        NotificationType notificationType = NotificationType.LIKE;
        NotificationTarget notificationTarget = NotificationTarget.COMMENT;

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                TODOKTODOK_TITLE,
                notificationBody,
                commentLikeCreated.discussionId(),
                commentLikeCreated.commentId(),
                replyId,
                commentLikeCreated.authorNickname(),
                commentLikeCreated.discussionTitle(),
                content,
                notificationType.name(),
                notificationTarget.name()
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);

        final CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest(
                commentLikeCreated.commentMemberId(),
                commentLikeCreated.discussionId(),
                commentLikeCreated.commentId(),
                replyId,
                commentLikeCreated.authorNickname(),
                commentLikeCreated.discussionTitle(),
                content,
                notificationType,
                notificationTarget
        );
        notificationCommandService.createNotification(createNotificationRequest);
    }
}
