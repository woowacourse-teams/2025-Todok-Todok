package todoktodok.backend.discussion.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionLike;
import todoktodok.backend.member.domain.Member;

public interface DiscussionLikeRepository extends JpaRepository<DiscussionLike, Long> {

    Optional<DiscussionLike> findByMemberAndDiscussion(final Member member, final Discussion discussion);
}
