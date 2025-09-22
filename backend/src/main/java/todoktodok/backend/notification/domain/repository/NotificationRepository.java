package todoktodok.backend.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
