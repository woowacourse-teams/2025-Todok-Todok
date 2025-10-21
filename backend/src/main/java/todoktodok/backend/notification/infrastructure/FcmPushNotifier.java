package todoktodok.backend.notification.infrastructure;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import todoktodok.backend.notification.domain.NotificationToken;
import todoktodok.backend.notification.domain.repository.NotificationTokenRepository;

@Slf4j
@Component
@AllArgsConstructor
public class FcmPushNotifier {

    private static final String TODOKTODOK_LOGO_URL = "https://techcourse-project-2025.s3.ap-northeast-2.amazonaws.com/todoktodok-images/profile/todoki.png";
    private static final Set<MessagingErrorCode> DELETABLE_ERRORS = Set.of(
            MessagingErrorCode.UNREGISTERED,
            MessagingErrorCode.INVALID_ARGUMENT
    );
    private static final Set<MessagingErrorCode> RETRYABLE_ERRORS = Set.of(
            MessagingErrorCode.UNAVAILABLE,
            MessagingErrorCode.QUOTA_EXCEEDED,
            MessagingErrorCode.INTERNAL
    );

    private final NotificationTokenRepository notificationTokenRepository;

    public void sendPush(
            final Long recipientId,
            final FcmMessagePayload fcmMessagePayload
    ) {
        log.info("fake send push to {}", recipientId);
//        final List<NotificationToken> notificationTokens = notificationTokenRepository.findAllByMemberId(recipientId);
//        final List<String> tokens = notificationTokens.stream()
//                .map(NotificationToken::getToken)
//                .toList();
//
//        final MulticastMessage multicastMessage = MulticastMessage.builder()
//                .addAllTokens(tokens)
//                .putData("notificationId", fcmMessagePayload.notificationId())
//                .putData("title", fcmMessagePayload.title())
//                .putData("body", fcmMessagePayload.body())
//                .putData("image", TODOKTODOK_LOGO_URL)
//                .putData("discussionId", fcmMessagePayload.discussionId())
//                .putData("commentId", fcmMessagePayload.commentId())
//                .putData("replyId", fcmMessagePayload.replyId())
//                .putData("memberNickname", fcmMessagePayload.memberNickname())
//                .putData("discussionTitle", fcmMessagePayload.discussionTitle())
//                .putData("content", fcmMessagePayload.content())
//                .putData("type", fcmMessagePayload.type())
//                .putData("target", fcmMessagePayload.target())
//                .build();
//
//        try {
//            final BatchResponse batchResponse = FirebaseMessaging.getInstance()
//                    .sendEachForMulticast(multicastMessage);
//
//            handleResponses(batchResponse, tokens);
//        } catch (final FirebaseMessagingException e) {
//            log.error("Fail sending message to FCM");
//        }
    }

    private void handleResponses(
            final BatchResponse batchResponse,
            final List<String> tokens
    ) {
        final List<SendResponse> responses = batchResponse.getResponses();
        final List<String> deletedTokens = new ArrayList<>();

        for (int i = 0; i < responses.size(); i++) {
            final String token = tokens.get(i);
            final SendResponse sendResponse = responses.get(i);

            if (sendResponse.isSuccessful()) {
                log.info(
                        String.format("푸시 요청을 성공적으로 보냈습니다: token= %s", token)
                );
                continue;
            }

            final MessagingErrorCode errorCode = sendResponse.getException().getMessagingErrorCode();
            if (DELETABLE_ERRORS.contains(errorCode)) {
                deletedTokens.add(token);
                log.info(
                        String.format("푸시 요청 전송에 실패해 토큰을 삭제했습니다: token= %s, errorCode= %s", maskToken(token), errorCode.name())
                );
            }

            if (RETRYABLE_ERRORS.contains(errorCode)) {
                // TODO 재시도 처리 구현 예정
            }
        }

        notificationTokenRepository.deleteAllByTokenIn(deletedTokens);
    }

    private String maskToken(final String token) {
        if (token == null || token.length() < 8) {
            return "****";
        }
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }
}
