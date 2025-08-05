package todoktodok.backend.reply.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.ReplyReport;

public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

    boolean existsByMemberAndReply(final Member member, final Reply reply);
}
