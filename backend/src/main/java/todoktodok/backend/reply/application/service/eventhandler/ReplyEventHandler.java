package todoktodok.backend.reply.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;
import todoktodok.backend.reply.application.service.command.ReplyCreated;
import todoktodok.backend.reply.application.service.command.ReplyLikeCreated;

@Component
@AllArgsConstructor
public class ReplyEventHandler {

    public static final String HEART = "❤\uFE0F";
    public static final String TODOKTODOK_TITLE = "토독토독";

    private final FcmPushNotifier fcmPushNotifier;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final ReplyCreated replyCreated) {
        final Long recipientId = replyCreated.commentMemberId();
        final Long authorId = replyCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("[%s님의 대댓글 도착] %s",
                replyCreated.authorNickname(),
                replyCreated.discussionTitle()
        );

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                TODOKTODOK_TITLE,
                notificationBody,
                replyCreated.discussionId(),
                replyCreated.commentId(),
                replyCreated.replyId(),
                replyCreated.authorNickname(),
                replyCreated.discussionTitle(),
                replyCreated.content(),
                "REPLY",
                "REPLY"
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final ReplyLikeCreated replyLikeCreated) {
        final Long recipientId = replyLikeCreated.replyMemberId();
        final Long authorId = replyLikeCreated.authorId();

        if (recipientId.equals(authorId)) {
            return;
        }

        final String notificationBody = String.format("%s 님이 [%s] 토론방의 대댓글에 %s를 보냈습니다",
                replyLikeCreated.authorNickname(),
                replyLikeCreated.discussionTitle(),
                HEART
        );
        final String content = "";

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                replyLikeCreated.discussionId(),
                replyLikeCreated.commentId(),
                replyLikeCreated.replyId(),
                replyLikeCreated.authorNickname(),
                replyLikeCreated.discussionTitle(),
                content,
                "LIKE",
                "REPLY"
        );
        fcmPushNotifier.sendPush(recipientId, fcmMessagePayload);
    }
}
