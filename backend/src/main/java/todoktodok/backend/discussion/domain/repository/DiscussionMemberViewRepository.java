package todoktodok.backend.discussion.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionMemberView;
import todoktodok.backend.member.domain.Member;

public interface DiscussionMemberViewRepository extends JpaRepository<DiscussionMemberView, Long> {

    Optional<DiscussionMemberView> findByMemberAndDiscussion(final Member member, final Discussion discussion);
}
