package todoktodok.backend.notification.application.service.command;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.domain.Notification;
import todoktodok.backend.notification.domain.repository.NotificationRepository;

@Service
@Transactional
@AllArgsConstructor
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public void createNotification(final CreateNotificationRequest request) {
        final Member recipient = findMember(request.recipientId());

        final Notification notification = Notification.builder()
                .recipient(recipient)
                .discussionId(request.discussionId())
                .commentId(request.commentId())
                .replyId(request.replyId())
                .memberNickname(request.memberNickname())
                .discussionTitle(request.discussionTitle())
                .content(request.content())
                .notificationType(request.type())
                .notificationTarget(request.target())
                .build();

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getNotifications(final Long memberId) {
        final Member recipient = findMember(memberId);
        final Long notReadCount = notificationRepository.countNotificationByRecipientAndIsReadFalse(recipient);
        final List<Notification> notifications = notificationRepository.findNotificationsByRecipient(recipient);

        return notifications.stream()
                .map(notification -> new NotificationResponse(notReadCount, notification))
                .toList();
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new IllegalStateException(String.format("해당하는 회원을 찾을 수 없습니다 : recipientId = %d", memberId)));
    }
}
