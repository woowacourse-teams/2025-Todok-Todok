package todoktodok.backend.reply.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;
import todoktodok.backend.reply.application.service.command.ReplyCreated;
import todoktodok.backend.reply.application.service.command.ReplyLikeCreated;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.ReplyLike;

@Component
@AllArgsConstructor
public class ReplyEventHandler {

    private final FcmPushNotifier fcmPushNotifier;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final ReplyCreated replyCreated) {
        final Discussion discussion = replyCreated.discussion();
        final Member recipient = discussion.getMember();
        final Comment comment = replyCreated.comment();
        final Reply reply = replyCreated.reply();
        final Member author = reply.getMember();
        final String discussionTitle = discussion.getTitle();
        final String notificationBody = String.format("[%s님의 대댓글 도착] %s", author, discussionTitle);
        final Long discussionId = discussion.getId();
        final Long commentId = comment.getId();
        final Long replyId = reply.getId();
        final String memberNickname = author.getNickname();
        final String content = reply.getContent();

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                discussionId,
                commentId,
                replyId,
                memberNickname,
                discussionTitle,
                content,
                "REPLY",
                "REPLY"
        );
        fcmPushNotifier.sendPush(recipient, fcmMessagePayload);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final ReplyLikeCreated replyLikeCreated) {
        final ReplyLike replyLike = replyLikeCreated.replyLike();
        final Reply reply = replyLike.getReply();
        final Comment comment = reply.getComment();
        final Discussion discussion = comment.getDiscussion();
        final Member recipient = reply.getMember();
        final Member author = replyLike.getMember();
        final String discussionTitle = discussion.getTitle();
        final String notificationBody = String.format("%s 님이 [%s] 토론방의 대댓글에 ❤\uFE0F를 보냈습니다", author, discussionTitle);
        final Long discussionId = discussion.getId();
        final Long commentId = comment.getId();
        final Long replyId = reply.getId();
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
                "REPLY"
        );
        fcmPushNotifier.sendPush(recipient, fcmMessagePayload);
    }
}
