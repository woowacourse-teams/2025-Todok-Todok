package todoktodok.backend.notification.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.notification.application.dto.response.NotificationCheckResponse;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.domain.Notification;
import todoktodok.backend.notification.domain.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public List<NotificationResponse> getNotifications(final Long memberId) {
        final Member recipient = findMember(memberId);
        final Long notReadCount = notificationRepository.countNotificationByRecipientAndIsReadFalse(recipient);
        final List<Notification> notifications = notificationRepository.findNotificationsByRecipient(recipient);

        return notifications.stream()
                .map(notification -> new NotificationResponse(notReadCount, notification))
                .toList();
    }

    public NotificationCheckResponse existsUnReadNotification(
            final Long memberId
    ) {
        final Member member = findMember(memberId);
        boolean existsNonReadNotification = notificationRepository.existsByRecipientAndIsReadFalse(member);

        return new NotificationCheckResponse(existsNonReadNotification);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다 : recipientId = %d", memberId)
                        )
                );
    }
}
