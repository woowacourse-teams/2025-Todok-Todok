package todoktodok.backend.notification.application.service.command;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.domain.Notification;
import todoktodok.backend.notification.domain.repository.NotificationRepository;
import todoktodok.backend.notification.exception.NotificationForbiddenException;

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

    public void markNotificationAsRead(
            final Long memberId,
            final Long notificationId
    ) {
        final Member member = findMember(memberId);
        final Notification notification = findNotification(notificationId);

        if (!notification.equalsRecipient(member)) {
            throw new NotificationForbiddenException("본인 알림이 아닙니다.");
        }

        notification.update(true);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당하는 회원을 찾을 수 없습니다 : recipientId = %d", memberId)
                        )
                );
    }

    private Notification findNotification(final Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당하는 알림을 찾을 수 없습니다 : notificationId = %d", notificationId)
                        )
                );
    }
}
