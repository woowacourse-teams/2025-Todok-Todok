package todoktodok.backend.discussion.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    List<Discussion> findDiscussionsByMember(final Member member);

    @Query("""
        SELECT d FROM Discussion d
        WHERE UPPER(d.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        UNION
        SELECT d FROM Discussion d
        JOIN d.book b
        WHERE UPPER(b.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        AND b.deletedAt IS NULL
    """)
    List<Discussion> searchByKeyword(
            @Param("keyword") final String keyword
    );

    @Query("""
        SELECT d FROM Discussion d
        WHERE UPPER(d.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        AND d.member = :member
        UNION
        SELECT d FROM Discussion d
        JOIN d.book b
        WHERE UPPER(b.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        AND b.deletedAt IS NULL
        AND d.member = :member
    """)
    List<Discussion> searchByKeywordAndMember(
            @Param("keyword") final String keyword,
            @Param("member") final Member member
    );
}
