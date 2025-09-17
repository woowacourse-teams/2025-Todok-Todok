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

    List<NotificationToken> findAllByMemberId(final Long memberId);

    boolean existsByToken(final String token);

    boolean existsByTokenAndMember(final String token, final Member member);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteAllByTokenIn(@Param("tokens") final Collection<String> tokens);

    void deleteByFidAndMember(final String fid, final Member member);
}
