package todoktodok.backend.member.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(final String email);

    Optional<Member> findByIdAndDeletedAtIsNull(final Long id);

    Optional<Member> findByEmailAndDeletedAtIsNull(final String email);

    boolean existsByIdAndDeletedAtIsNull(final Long id);

    boolean existsByNicknameAndDeletedAtIsNull(final String nickname);

    boolean existsByEmailAndDeletedAtIsNull(final String email);
}
