package todoktodok.backend.comment.application.service.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import todoktodok.backend.comment.application.service.command.CommentCreated;
import todoktodok.backend.comment.application.service.command.CommentLikeCreated;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.CommentLike;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.infrastructure.FcmMessagePayload;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@Component
@AllArgsConstructor
public class CommentEventHandler {

    private final FcmPushNotifier fcmPushNotifier;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final CommentCreated commentCreated) {
        final Discussion discussion = commentCreated.discussion();
        final Member recipient = discussion.getMember();
        final Comment comment = commentCreated.comment();
        final Member author = comment.getMember();
        final String authorNickname = author.getNickname();
        final String discussionTitle = discussion.getTitle();
        final String notificationBody = String.format("[%s님의 댓글 도착] %s", authorNickname, discussionTitle);
        final Long discussionId = discussion.getId();
        final Long commentId = comment.getId();
        final Long replyId = null;
        final String memberNickname = author.getNickname();
        final String content = comment.getContent();

        final FcmMessagePayload fcmMessagePayload = new FcmMessagePayload(
                "토독토독",
                notificationBody,
                discussionId,
                commentId,
                replyId,
                memberNickname,
                discussionTitle,
                content,
                "COMMENT",
                "COMMENT"
        );
        fcmPushNotifier.sendPush(recipient, fcmMessagePayload);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPushOn(final CommentLikeCreated commentLikeCreated) {
        final CommentLike commentLike = commentLikeCreated.commentLike();
        final Comment comment = commentLike.getComment();
        final Discussion discussion = comment.getDiscussion();
        final Member recipient = comment.getMember();
        final Member author = commentLike.getMember();
        final String authorNickname = author.getNickname();
        final String discussionTitle = discussion.getTitle();
        final String notificationBody = String.format("%s 님이 [%s] 토론방의 댓글에 ❤\uFE0F를 보냈습니다", authorNickname, discussionTitle);
        final Long discussionId = discussion.getId();
        final Long commentId = comment.getId();
        final Long replyId = null;
        final String memberNickname = author.getNickname();
        final String content = "";

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
                "COMMENT"
        );
        fcmPushNotifier.sendPush(recipient, fcmMessagePayload);
    }
}
