package todoktodok.backend.discussion.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.discussion.application.service.command.DiscussionLikeCreated;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Component
@AllArgsConstructor
public class DiscussionEventHandler {

    private final FcmPushNotifier fcmPushNotifier;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final DiscussionLikeCreated discussionLikeCreated) {
        final Long recipientId = discussionLikeCreated.discussionMemberId();
        final Long authorId = discussionLikeCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("%s 님이 [%s] 토론방에 ❤\uFE0F를 보냈습니다",
                discussionLikeCreated.authorNickname(),
                discussionLikeCreated.discussionTitle()
        );
        final Long commentId = null;
        final Long replyId = null;
        final String content = "";

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                discussionLikeCreated.discussionId(),
                commentId,
                replyId,
                discussionLikeCreated.authorNickname(),
                discussionLikeCreated.discussionTitle(),
                content,
                "LIKE",
                "DISCUSSION"
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);
    }
}
