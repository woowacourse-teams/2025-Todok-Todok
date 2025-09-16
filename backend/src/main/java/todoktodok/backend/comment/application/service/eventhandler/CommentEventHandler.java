package todoktodok.backend.comment.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.comment.application.service.command.CommentCreated;
import todoktodok.backend.comment.application.service.command.CommentLikeCreated;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Component
@AllArgsConstructor
public class CommentEventHandler {

    private final FcmPushNotifier fcmPushNotifier;

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

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                commentCreated.discussionId(),
                commentCreated.commentId(),
                replyId,
                commentCreated.authorNickname(),
                commentCreated.discussionTitle(),
                commentCreated.content(),
                "COMMENT",
                "COMMENT"
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final CommentLikeCreated commentLikeCreated) {
        final Long recipientId = commentLikeCreated.commentMemberId();
        final Long authorId = commentLikeCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("%s 님이 [%s] 토론방의 댓글에 ❤\uFE0F를 보냈습니다",
                commentLikeCreated.commentMemberId(),
                commentLikeCreated, commentLikeCreated.discussionTitle()
        );
        final Long replyId = null;
        final String content = "";

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                commentLikeCreated.discussionId(),
                commentLikeCreated.commentId(),
                replyId,
                commentLikeCreated.authorNickname(),
                commentLikeCreated.discussionTitle(),
                content,
                "LIKE",
                "COMMENT"
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);
    }
}
