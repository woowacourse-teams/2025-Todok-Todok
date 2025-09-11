package todoktodok.backend.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.notification.domain.NotificationToken;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {
    boolean existsByToken(String token);
}
