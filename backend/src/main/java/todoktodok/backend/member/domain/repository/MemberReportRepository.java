package todoktodok.backend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.MemberReport;

public interface MemberReportRepository extends JpaRepository<MemberReport, Long> {
}
