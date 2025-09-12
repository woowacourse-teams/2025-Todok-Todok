package todoktodok.backend.notification.infrastructure;

import com.google.firebase.messaging.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.domain.NotificationToken;
import todoktodok.backend.notification.domain.repository.NotificationTokenRepository;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class FcmPushNotifier {

    private static final String TODOKTODOK_LOGO_URL = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/todoktodok-images/profile/todoki.png";

    private final NotificationTokenRepository notificationTokenRepository;

    public void sendPush(
            final Member member,
            final FcmMessagePayload fcmMessagePayload
    ) {
        final List<NotificationToken> notificationTokens = notificationTokenRepository.findAllByMember(member);
        final List<String> tokens = notificationTokens.stream()
                .map(NotificationToken::getToken)
                .toList();

        final MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle(fcmMessagePayload.title())
                        .setBody(fcmMessagePayload.body())
                        .setImage(TODOKTODOK_LOGO_URL)
                        .build())
                .putData("discussionId", fcmMessagePayload.discussionId())
                .putData("commentId", fcmMessagePayload.commentId())
                .putData("replyId", fcmMessagePayload.replyId())
                .putData("memberNickname", fcmMessagePayload.memberNickname())
                .putData("discussionTitle", fcmMessagePayload.discussionTitle())
                .putData("content", fcmMessagePayload.content())
                .putData("type", fcmMessagePayload.type())
                .putData("target", fcmMessagePayload.target())
                .build();

        try {
            final BatchResponse batchResponse = FirebaseMessaging.getInstance()
                    .sendEachForMulticast(multicastMessage);

            // TODO 에러 핸들링
        } catch (final FirebaseMessagingException e) {
            log.error("Fail sending message to FCM");
        }
    }
}
