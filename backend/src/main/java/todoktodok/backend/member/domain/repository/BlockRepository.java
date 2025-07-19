package todoktodok.backend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
