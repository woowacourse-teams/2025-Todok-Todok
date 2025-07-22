package todoktodok.backend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Block;
import todoktodok.backend.member.domain.Member;

public interface BlockRepository extends JpaRepository<Block, Long> {

    boolean existsByMemberAndTarget(final Member member, final Member target);
}
