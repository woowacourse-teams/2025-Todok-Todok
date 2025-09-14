package todoktodok.backend.notification.domain.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.notification.domain.NotificationToken;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

    boolean existsByToken(final String token);

    void deleteByFidAndMember(final String fid, final Member member);

    List<NotificationToken> findAllByMember(final Member member);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteAllByTokenIn(@Param("tokens") Collection<String> tokens);
}
