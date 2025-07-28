package todoktodok.backend.discussion.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    List<Discussion> findDiscussionsByMember(Member member);

    @Query("""
        select d from Discussion d
        where upper(d.title) like upper(concat('%', :keyword, '%'))
        and d.deletedAt is null
        union
        select d from Discussion d
        join d.book b
        where upper(b.title) like upper(concat('%', :keyword, '%'))
        and d.deletedAt is null
        and b.deletedAt is null
    """)
    List<Discussion> searchByKeyword(
            @Param("keyword") String keyword
    );

    @Query("""
        select d from Discussion d
        where upper(d.title) like upper(concat('%', :keyword, '%'))
        and d.deletedAt is null
        and d.member = :member
        union
        select d from Discussion d
        join d.book b
        where upper(b.title) like upper(concat('%', :keyword, '%'))
        and d.deletedAt is null
        and b.deletedAt is null
        and d.member = :member
    """)
    List<Discussion> searchByKeywordAndMember(
            @Param("keyword") String keyword,
            @Param("member") Member member
    );
}
