package todoktodok.backend.notification.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Long countNotificationByRecipientAndIsReadFalse(Member recipient);

    List<Notification> findNotificationsByRecipient(Member recipient);
}
