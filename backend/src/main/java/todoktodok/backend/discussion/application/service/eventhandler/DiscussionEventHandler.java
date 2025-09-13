package todoktodok.backend.discussion.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.discussion.application.service.command.DiscussionLikeCreated;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionLike;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Component
@AllArgsConstructor
public class DiscussionEventHandler {

    private final FcmPushNotifier fcmPushNotifier;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final DiscussionLikeCreated discussionLikeCreated) {
        final DiscussionLike discussionLike = discussionLikeCreated.discussionLike();
        final Discussion discussion = discussionLike.getDiscussion();
        final Member recipient = discussion.getMember();
        final Member author = discussionLike.getMember();
        final String discussionTitle = discussion.getTitle();
        final String notificationBody = String.format("%s 님이 [%s] 토론방에 ❤\uFE0F를 보냈습니다", author, discussionTitle);
        final Long discussionId = discussion.getId();
        final Long commentId = null;
        final Long replyId = null;
        final String memberNickname = author.getNickname();
        final String content = null;

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                discussionId,
                commentId,
                replyId,
                memberNickname,
                discussionTitle,
                content,
                "LIKE",
                "DISCUSSION"
        );
        fcmPushNotifier.sendPush(recipient, fcmMessagePayload);
    }
}
