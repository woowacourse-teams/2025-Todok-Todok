package todoktodok.backend.member.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import todoktodok.backend.member.domain.Block;
import todoktodok.backend.member.domain.Member;

public interface BlockRepository extends JpaRepository<Block, Long> {

    boolean existsByMemberAndTarget(final Member member, final Member target);

    @Query("""
        SELECT b FROM Block b
        WHERE b.member = :member
    """)
    List<Block> findBlocksByMember(final Member member);
}
