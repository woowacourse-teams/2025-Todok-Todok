package todoktodok.backend.notification.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.notification.application.dto.response.NotificationItemResponse;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.application.dto.response.UnreadNotificationResponse;
import todoktodok.backend.notification.domain.Notification;
import todoktodok.backend.notification.domain.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NotificationResponse getNotifications(final Long memberId) {
        final Member recipient = findMember(memberId);
        final Long unreadCount = notificationRepository.countNotificationByRecipientAndIsReadFalse(recipient);
        final List<Notification> notifications = notificationRepository.findNotificationsByRecipient(recipient);

        final List<NotificationItemResponse> notificationItemResponses = notifications.stream()
                .map(NotificationItemResponse::new)
                .toList();

        return new NotificationResponse(unreadCount, notificationItemResponses);
    }

    public UnreadNotificationResponse hasUnreadNotifications(final Long memberId) {
        final Member member = findMember(memberId);
        final boolean existsUnreadNotification = notificationRepository.existsByRecipientAndIsReadFalse(member);

        return new UnreadNotificationResponse(existsUnreadNotification);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다 : recipientId = %d", memberId)
                        )
                );
    }
}
