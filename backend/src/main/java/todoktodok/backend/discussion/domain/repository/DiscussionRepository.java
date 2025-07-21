package todoktodok.backend.discussion.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.discussion.domain.Discussion;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}
