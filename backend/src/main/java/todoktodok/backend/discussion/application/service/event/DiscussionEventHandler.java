package todoktodok.backend.discussion.application.service.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.discussion.application.service.command.DiscussionCommandService;
import todoktodok.backend.discussion.application.service.command.DiscussionLikeCreated;
import todoktodok.backend.notification.application.service.command.CreateNotificationRequest;
import todoktodok.backend.notification.application.service.command.NotificationCommandService;
import todoktodok.backend.notification.domain.NotificationTarget;
import todoktodok.backend.notification.domain.NotificationType;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Slf4j
@Component
@AllArgsConstructor
public class DiscussionEventHandler {

    public static final String TODOKTODOK_TITLE = "토독토독";
    public static final String HEART = "❤\uFE0F";

    private final FcmPushNotifier fcmPushNotifier;
    private final NotificationCommandService notificationCommandService;
    private final DiscussionCommandService discussionCommandService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final DiscussionLikeCreated discussionLikeCreated) {
        final Long recipientId = discussionLikeCreated.discussionMemberId();
        final Long authorId = discussionLikeCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("%s 님이 [%s] 토론방에 %s를 보냈습니다",
                discussionLikeCreated.authorNickname(),
                discussionLikeCreated.discussionTitle(),
                HEART
        );
        final Long commentId = null;
        final Long replyId = null;
        final String content = "";
        final NotificationType notificationType = NotificationType.LIKE;
        final NotificationTarget notificationTarget = NotificationTarget.DISCUSSION;

        final CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest(
                discussionLikeCreated.discussionMemberId(),
                discussionLikeCreated.discussionId(),
                commentId,
                replyId,
                discussionLikeCreated.authorNickname(),
                discussionLikeCreated.discussionTitle(),
                content,
                notificationType,
                notificationTarget
        );

        final Long notificationId = notificationCommandService.createNotification(createNotificationRequest);

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                notificationId,
                TODOKTODOK_TITLE,
                notificationBody,
                discussionLikeCreated.discussionId(),
                commentId,
                replyId,
                discussionLikeCreated.authorNickname(),
                discussionLikeCreated.discussionTitle(),
                content,
                notificationType.name(),
                notificationTarget.name()
        );

        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDiscussionView(final DiscussionViewEvent discussionViewEvent) {
        try {
            log.info("DiscussionViewEvent 실행");
            discussionCommandService.updateDiscussionMemberView(discussionViewEvent.memberId(),
                    discussionViewEvent.discussionId());
        } catch (final Exception e) {
            log.error("비동기 처리 중 오류 발생, cause: {}", e.getMessage(), e);
        }
    }
}
