package todoktodok.backend.member.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final String email);

    boolean existsByNickname(final String nickname);

    boolean existsByEmail(final String email);

    boolean existsById(final Long id);
}
