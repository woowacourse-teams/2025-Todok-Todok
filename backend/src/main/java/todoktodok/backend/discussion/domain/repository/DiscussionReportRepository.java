package todoktodok.backend.discussion.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionReport;
import todoktodok.backend.member.domain.Member;

public interface DiscussionReportRepository extends JpaRepository<DiscussionReport, Long> {

    boolean existsByDiscussionAndMember(final Discussion discussion, final Member member);
}
