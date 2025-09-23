package todoktodok.backend.notification.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Long countNotificationByRecipientAndIsReadFalse(final Member recipient);

    List<Notification> findNotificationsByRecipient(final Member recipient);

    Notification findNotificationById(final Long id);

    Notification findByIdAndMember(final Long id, final Member recipient);
}
