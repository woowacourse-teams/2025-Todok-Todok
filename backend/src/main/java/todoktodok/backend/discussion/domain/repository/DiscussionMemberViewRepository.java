package todoktodok.backend.discussion.domain.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionMemberView;
import todoktodok.backend.member.domain.Member;

public interface DiscussionMemberViewRepository extends JpaRepository<DiscussionMemberView, Long> {

    Optional<DiscussionMemberView> findByMemberAndDiscussion(final Member member, final Discussion discussion);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE DiscussionMemberView dmv
            SET dmv.modifiedAt = :now
            WHERE dmv.id = :id
            """)
    void updateModifiedAtById(@Param("id") final Long id, @Param("now") final LocalDateTime now);
}
