package todoktodok.backend.comment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.CommentReport;
import todoktodok.backend.member.domain.Member;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    boolean existsByMemberAndComment(final Member member, final Comment comment);
}
