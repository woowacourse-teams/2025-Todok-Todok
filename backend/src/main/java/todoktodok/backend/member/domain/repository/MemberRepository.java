package todoktodok.backend.member.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = """
        SELECT *
        FROM member
        WHERE id = :id
        """, nativeQuery = true)
    Optional<Member> findAnyById(@Param("id") final Long id);

    Optional<Member> findByEmail(final String email);

    boolean existsByNickname(final String nickname);

    boolean existsByEmail(final String email);

    boolean existsById(final Long id);
}
